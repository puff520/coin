package cn.ztuo.bitrade.model.screen;

import cn.ztuo.bitrade.model.screen.AccountScreen;
import lombok.Data;

@Data
public class MemberDepositScreen extends AccountScreen {

    private String address ;

    private String unit ;
}
