package cn.ztuo.bitrade.controller.promotion;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.alibaba.fastjson.JSONObject;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.sparkframework.security.Encrypt;

import cn.ztuo.bitrade.annotation.AccessLog;
import cn.ztuo.bitrade.constant.AdminModule;
import cn.ztuo.bitrade.constant.BooleanEnum;
import cn.ztuo.bitrade.constant.PageModel;
import cn.ztuo.bitrade.constant.PromotionRewardType;
import cn.ztuo.bitrade.constant.SysConstant;
import cn.ztuo.bitrade.controller.base.BaseAdminController;
import cn.ztuo.bitrade.entity.Admin;
import cn.ztuo.bitrade.entity.Coin;
import cn.ztuo.bitrade.entity.QRewardPromotionSetting;
import cn.ztuo.bitrade.entity.RewardPromotionSetting;
import cn.ztuo.bitrade.service.CoinService;
import cn.ztuo.bitrade.service.LocaleMessageSourceService;
import cn.ztuo.bitrade.service.RewardPromotionSettingService;
import cn.ztuo.bitrade.util.MessageResult;


@RestController
@RequestMapping("promotion/reward")
public class RewardPromotionController extends BaseAdminController {

    @Autowired
    private RewardPromotionSettingService rewardPromotionSettingService;

    @Autowired
    private CoinService coinService ;

    @Value("${spark.system.md5.key}")
    private String md5Key;
    @Autowired
    private LocaleMessageSourceService messageSource;

    @RequiresPermissions("promotion:reward:merge")
    @PostMapping("merge")
    @AccessLog(module = AdminModule.SYSTEM, operation = "??????????????????????????????")
    public MessageResult merge(@Valid RewardPromotionSetting setting, @SessionAttribute(SysConstant.SESSION_ADMIN)Admin admin
                                ,String unit, @RequestParam(value = "password") String password) {
        password = Encrypt.MD5(password + md5Key);
       Assert.isTrue(password.equals(admin.getPassword()),messageSource.getMessage("WRONG_PASSWORD"));
        Coin coin = null ;
        if (setting.getType() != PromotionRewardType.EXCHANGE_TRANSACTION){
            coin = coinService.findByUnit(unit);
            setting.setCoin(coin);
        }
        RewardPromotionSetting rewardPromotionSetting = rewardPromotionSettingService.findByType(setting.getType());
        if(setting.getId()==null&&rewardPromotionSetting!=null) {
            return error("???????????????????????????");
        }
        setting.setInfo("{\"one\":"+setting.getOne()+",\"two\":"+setting.getTwo()+"}");
        rewardPromotionSettingService.save(setting);
        return MessageResult.success(messageSource.getMessage("SUCCESS"));
    }

    @RequiresPermissions("promotion:reward:detail")
    @PostMapping("detail")
    @AccessLog(module = AdminModule.SYSTEM, operation = "????????????????????????")
    public MessageResult detail(@RequestParam("id")Long id) {
        RewardPromotionSetting setting = rewardPromotionSettingService.findById(id);
        if(setting==null){
            return error("??????????????????");
        }
        String jsonStr = setting.getInfo();
        if(!StringUtils.isEmpty(jsonStr)){
            JSONObject json = JSONObject.parseObject(jsonStr);
            setting.setOne(json.getBigDecimal("one"));
            setting.setTwo(json.getBigDecimal("two"));
        }

        return success(messageSource.getMessage("SUCCESS"),setting);
    }

    /**
     * ????????????????????????????????????type?????????
     * ????????????updatetime??????
     *
     * @param enable
     * @param type
     * @return
     */
    @RequiresPermissions("promotion:reward:page-query")
    @GetMapping("page-query")
    @AccessLog(module = AdminModule.SYSTEM, operation = "??????????????????????????????")
    public MessageResult pageQuery(
            PageModel pageModel,
            @RequestParam(value = "status", defaultValue = "1") BooleanEnum enable,
            @RequestParam(value = "type", required = false) PromotionRewardType type) {
        BooleanExpression predicate = null;
        if (type != null) {
            predicate.andAnyOf(QRewardPromotionSetting.rewardPromotionSetting.type.eq(type));
        }
        Page<RewardPromotionSetting> all = rewardPromotionSettingService.findAll(predicate, pageModel);
        for(RewardPromotionSetting setting : all){
            if(StringUtils.isEmpty(setting.getInfo())) {
                continue ;
            }
            JSONObject jsonObject = JSONObject.parseObject(setting.getInfo());
            setting.setOne(jsonObject.getBigDecimal("one"));
            setting.setTwo(jsonObject.getBigDecimal("two"));
        }
        return success(all);
    }

    @RequiresPermissions("promotion:reward:deletes")
    @DeleteMapping("deletes")
    @AccessLog(module = AdminModule.SYSTEM, operation = "??????????????????????????????")
    @Transactional(rollbackFor = Exception.class)
    public MessageResult deletes(long[] ids) {
        Assert.notNull(ids, "ids?????????null");
        rewardPromotionSettingService.deletes(ids);
        return MessageResult.success(messageSource.getMessage("SUCCESS"));
    }
}
