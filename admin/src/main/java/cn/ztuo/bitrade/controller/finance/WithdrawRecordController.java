package cn.ztuo.bitrade.controller.finance;

import static cn.ztuo.bitrade.constant.BooleanEnum.IS_FALSE;
import static cn.ztuo.bitrade.constant.WithdrawStatus.FAIL;
import static cn.ztuo.bitrade.constant.WithdrawStatus.SUCCESS;
import static cn.ztuo.bitrade.constant.WithdrawStatus.WAITING;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notNull;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.querydsl.core.types.Predicate;
import com.sparkframework.security.Encrypt;

import cn.ztuo.bitrade.annotation.AccessLog;
import cn.ztuo.bitrade.constant.AdminModule;
import cn.ztuo.bitrade.constant.BooleanEnum;
import cn.ztuo.bitrade.constant.PageModel;
import cn.ztuo.bitrade.constant.SysConstant;
import cn.ztuo.bitrade.constant.TransactionType;
import cn.ztuo.bitrade.constant.WithdrawStatus;
import cn.ztuo.bitrade.controller.base.BaseAdminController;
import cn.ztuo.bitrade.entity.Admin;
import cn.ztuo.bitrade.entity.Member;
import cn.ztuo.bitrade.entity.MemberTransaction;
import cn.ztuo.bitrade.entity.MemberWallet;
import cn.ztuo.bitrade.entity.QMember;
import cn.ztuo.bitrade.entity.QWithdrawRecord;
import cn.ztuo.bitrade.entity.WithdrawRecord;
import cn.ztuo.bitrade.es.ESUtils;
import cn.ztuo.bitrade.model.screen.WithdrawRecordScreen;
import cn.ztuo.bitrade.service.LocaleMessageSourceService;
import cn.ztuo.bitrade.service.MemberService;
import cn.ztuo.bitrade.service.MemberTransactionService;
import cn.ztuo.bitrade.service.MemberWalletService;
import cn.ztuo.bitrade.service.WithdrawRecordService;
import cn.ztuo.bitrade.util.MessageResult;
import cn.ztuo.bitrade.vo.WithdrawRecordVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GS
 * @description ??????
 * @date 2018/2/25 11:22
 */
@RestController
@Slf4j
@RequestMapping("/finance/withdraw-record")
public class WithdrawRecordController extends BaseAdminController {
    @Autowired
    private WithdrawRecordService withdrawRecordService;

    @Autowired
    private MemberWalletService memberWalletService;

    @Autowired
    private MemberTransactionService memberTransactionService;

    @Autowired
    private LocaleMessageSourceService messageSource;
    @Autowired
    private ESUtils esUtils;

    @Autowired
    private MemberService memberService;


    @Value("${spark.system.md5.key}")
    private String md5Key;

    @RequiresPermissions("finance:withdraw-record:all")
    @GetMapping("/all")
    @AccessLog(module = AdminModule.FINANCE, operation = "??????????????????WithdrawRecord")
    public MessageResult all() {
        List<WithdrawRecord> withdrawRecordList = withdrawRecordService.findAll();
        if (withdrawRecordList == null || withdrawRecordList.size() < 1) {
            return error(messageSource.getMessage("NO_DATA"));
        }
        return success(withdrawRecordList);
    }

    @RequiresPermissions(value = {"finance:withdraw-record:page-query", "finance:withdraw-record:page-query:success"}, logical = Logical.OR)
    @RequestMapping("/page-query")
    @AccessLog(module = AdminModule.FINANCE, operation = "????????????????????????WithdrawRecord")
    public MessageResult pageQuery(
            PageModel pageModel,
            WithdrawRecordScreen screen) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(QWithdrawRecord.withdrawRecord.memberId.eq(QMember.member.id));

        if (screen.getMemberId() != null) {
            predicates.add(QWithdrawRecord.withdrawRecord.memberId.eq(screen.getMemberId()));
        }

        if ( !StringUtils.isEmpty(screen.getMobilePhone())){
            Member member = memberService.findByPhone(screen.getMobilePhone());
            predicates.add(QWithdrawRecord.withdrawRecord.memberId.eq(member.getId()));
        }

        if ( !StringUtils.isEmpty(screen.getOrderSn())){
            predicates.add(QWithdrawRecord.withdrawRecord.transactionNumber.eq(screen.getOrderSn()));
        }

        if (screen.getStatus() != null) {
            predicates.add(QWithdrawRecord.withdrawRecord.status.eq(screen.getStatus()));
        }

        if (screen.getIsAuto() != null) {
            predicates.add(QWithdrawRecord.withdrawRecord.isAuto.eq(screen.getIsAuto()));
        }

        if (!StringUtils.isEmpty(screen.getAddress())) {
            predicates.add(QWithdrawRecord.withdrawRecord.address.eq(screen.getAddress()));
        }

        if (!StringUtils.isEmpty(screen.getUnit())) {
            predicates.add(QWithdrawRecord.withdrawRecord.coin.unit.equalsIgnoreCase(screen.getUnit()));
        }

        if (!StringUtils.isEmpty(screen.getAccount())) {
            predicates.add(QMember.member.username.like("%" + screen.getAccount() + "%")
                    .or(QMember.member.realName.like("%" + screen.getAccount() + "%")));
        }

        Page<WithdrawRecordVO> pageListMapResult = withdrawRecordService.joinFind(predicates, pageModel);
        return success(pageListMapResult);
    }

    @GetMapping("/{id}")
    @RequiresPermissions("finance:withdraw-record:detail")
    @AccessLog(module = AdminModule.FINANCE, operation = "????????????WithdrawRecord ??????")
    public MessageResult detail(@PathVariable("id") Long id) {
        WithdrawRecord withdrawRecord = withdrawRecordService.findOne(id);
        notNull(withdrawRecord, messageSource.getMessage("NO_DATA"));
        return success(withdrawRecord);
    }

    //??????????????????
    @RequiresPermissions("finance:withdraw-record:audit-pass")
    @PatchMapping("/audit-pass")
    @AccessLog(module = AdminModule.FINANCE, operation = "????????????WithdrawRecord??????????????????")
    public MessageResult auditPass(@RequestParam("ids") Long[] ids) {
        withdrawRecordService.audit(ids, WAITING);
        return success(messageSource.getMessage("PASS_THE_AUDIT"));
    }

    //?????????????????????
    @RequiresPermissions("finance:withdraw-record:audit-no-pass")
    @PatchMapping("/audit-no-pass")
    @AccessLog(module = AdminModule.FINANCE, operation = "????????????WithdrawRecord?????????????????????")
    public MessageResult auditNoPass(@RequestParam("ids") Long[] ids) {
        withdrawRecordService.audit(ids, FAIL);
        return success(messageSource.getMessage("AUDIT_DOES_NOT_PASS"));
    }

    /**
     * ???????????? ???????????????????????????
     *
     * @param id
     * @param transactionNumber
     * @return
     */
    @RequiresPermissions("finance:withdraw-record:add-transaction-number")
    @PatchMapping("/add-transaction-number")
    @AccessLog(module = AdminModule.FINANCE, operation = "?????????????????????")
    @Transactional(rollbackFor = Exception.class)
    public MessageResult addNumber(
            @RequestParam("id") Long id,
            @RequestParam("transactionNumber") String transactionNumber) {
        WithdrawRecord record = withdrawRecordService.findOne(id);
        Assert.notNull(record, "??????????????????");
        Assert.isTrue(record.getIsAuto() == BooleanEnum.IS_FALSE, "???????????????????????????");
        record.setTransactionNumber(transactionNumber);
        record.setStatus(WithdrawStatus.SUCCESS);
        MemberWallet memberWallet = memberWalletService.findByCoinAndMemberId(record.getCoin(), record.getMemberId());
        Assert.notNull(memberWallet, "member id " + record.getMemberId() + " ??? wallet ??? null");
        memberWallet.setFrozenBalance(memberWallet.getFrozenBalance().subtract(record.getTotalAmount()));
        memberWalletService.save(memberWallet);
        record = withdrawRecordService.save(record);

        MemberTransaction memberTransaction = new MemberTransaction();
        memberTransaction.setMemberId(record.getMemberId());
        memberTransaction.setAddress(record.getAddress());
        memberTransaction.setAmount(record.getTotalAmount());
        memberTransaction.setSymbol(record.getCoin().getUnit());
        memberTransaction.setCreateTime(record.getCreateTime());
        memberTransaction.setType(TransactionType.WITHDRAW);
        memberTransaction.setFee(record.getFee());
        memberTransaction.setRealFee(record.getFee()+"");
        memberTransaction.setDiscountFee("0");
        memberTransaction= memberTransactionService.save(memberTransaction);

        return MessageResult.success(messageSource.getMessage("SUCCESS"), record);
    }

    //????????????
    @RequiresPermissions("finance:withdraw-record:remittance")
    @PatchMapping("/remittance")
    @AccessLog(module = AdminModule.FINANCE, operation = "????????????/????????????")
    @Transactional(rollbackFor = Exception.class)
    public MessageResult remittance(
            @SessionAttribute(SysConstant.SESSION_ADMIN) Admin admin,
            @RequestParam("ids") Long[] ids,
            @RequestParam("transactionNumber") String transactionNumber,
            @RequestParam("password") String password) {
        Assert.notNull(admin, messageSource.getMessage("DATA_EXPIRED_LOGIN_AGAIN"));
        password = Encrypt.MD5(password + md5Key);
        if (!password.equals(admin.getPassword())) {
            return error(messageSource.getMessage("WRONG_PASSWORD"));
        }
        WithdrawRecord withdrawRecord;
        for (Long id : ids) {
            withdrawRecord = withdrawRecordService.findOne(id);
            notNull(withdrawRecord, "id :" + id + messageSource.getMessage("NO_DATA"));
            isTrue(withdrawRecord.getStatus() == WAITING, "??????????????????????????????,????????????!");
            isTrue(withdrawRecord.getIsAuto() == IS_FALSE, "????????????????????????!");
            //??????????????????
            withdrawRecord.setStatus(SUCCESS);
            //????????????
            withdrawRecord.setTransactionNumber(transactionNumber);
            MemberWallet memberWallet = memberWalletService.findByCoinAndMemberId(withdrawRecord.getCoin(), withdrawRecord.getMemberId());
            Assert.notNull(memberWallet, "member id " + withdrawRecord.getMemberId() + " ??? wallet ??? null");
            memberWallet.setFrozenBalance(memberWallet.getFrozenBalance().subtract(withdrawRecord.getTotalAmount()));
            memberWalletService.save(memberWallet);
            withdrawRecordService.save(withdrawRecord);

            MemberTransaction memberTransaction = new MemberTransaction();
            memberTransaction.setMemberId(withdrawRecord.getMemberId());
            memberTransaction.setAddress(withdrawRecord.getAddress());
            memberTransaction.setAmount(withdrawRecord.getTotalAmount());
            memberTransaction.setSymbol(withdrawRecord.getCoin().getUnit());
            memberTransaction.setCreateTime(withdrawRecord.getCreateTime());
            memberTransaction.setType(TransactionType.WITHDRAW);
            memberTransaction.setFee(withdrawRecord.getFee());
            memberTransaction.setRealFee(withdrawRecord.getFee()+"");
            memberTransaction.setDiscountFee("0");
            memberTransaction= memberTransactionService.save(memberTransaction);

        }
        return success();
    }

}
