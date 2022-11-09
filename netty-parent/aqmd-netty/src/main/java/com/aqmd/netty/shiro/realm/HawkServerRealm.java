package com.aqmd.netty.shiro.realm;

import com.aqmd.netty.entity.CustomerMsg;
import com.aqmd.netty.service.LoginUserService;
import com.aqmd.netty.shiro.util.PasswordUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.Realm;
import org.springframework.beans.factory.annotation.Autowired;

public class HawkServerRealm implements Realm {
   @Autowired
   private LoginUserService loginUserService;

   public String getName() {
      return "HawkServerRealm";
   }

   public boolean supports(AuthenticationToken token) {
      return token instanceof UsernamePasswordToken;
   }

   public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
      String loginNo = (String)token.getPrincipal();
      String password = new String((char[])((char[])token.getCredentials()));
      CustomerMsg dbUser = this.loginUserService.findUserByLoginNo(loginNo);
      if (dbUser == null) {
         throw new UnknownAccountException();
      } else {
         String dbPwd = dbUser.getPassword();
         String salt = dbUser.getSalt();
         String digestPwd = PasswordUtil.digestEncodedPassword(password, dbUser.getId() + salt);
         if (!dbPwd.equals(digestPwd)) {
            throw new IncorrectCredentialsException();
         } else {
            return new SimpleAuthenticationInfo(loginNo, password, this.getName());
         }
      }
   }

   public static void main(String[] args) {
      System.out.println("2ad18fc87f55c00ba273176a1349633453228dca55a8a9440b9f233a1b26cdd6bff6113206ec2ca2a1541a864e88167e404ff64eee40310c6eef5a420feb9308");
      System.out.println(PasswordUtil.digestEncodedPassword("d2f7575c5ea7c237725037a267c560f1", "9922286892116869133424271992021244351525401293731"));
   }
}
