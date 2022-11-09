package com.aqmd.netty.service;

import com.aqmd.netty.entity.CustomerMsg;
import java.util.HashMap;

public class DefaultLoginUserService implements LoginUserService {
   public CustomerMsg findUserByLoginNo(String loginNo) {
      new HashMap();
      CustomerMsg customerMsg = new CustomerMsg();
      customerMsg.setPassword("0c2eea5ef044ce91e0bf4191593c7c1e08126b428c29594de7df5cbdb74b4c90931ee1193b75e50bbc3f8e539605e75a3f2ce88a789d1bfbabf45a1ed2bce849");
      customerMsg.setSalt("123456");
      return customerMsg;
   }

   public Integer updPassword(String accountNo, String password) {
      return 1;
   }
}
