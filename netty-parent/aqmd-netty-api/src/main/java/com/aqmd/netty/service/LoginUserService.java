package com.aqmd.netty.service;

import com.aqmd.netty.entity.CustomerMsg;

public interface LoginUserService {
   CustomerMsg findUserByLoginNo(String loginNo);

   Integer updPassword(String accountNo, String password);
}
