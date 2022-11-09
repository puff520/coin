package com.aqmd.netty.shiro.util;

import org.apache.shiro.crypto.hash.ConfigurableHashService;
import org.apache.shiro.crypto.hash.DefaultHashService;
import org.apache.shiro.crypto.hash.HashRequest;

public class PasswordUtil {
   public static final String DEFAULT_ALGORITHM = "SHA-512";

   public static String digestEncodedPassword(final String passworld, String salt) {
      ConfigurableHashService hashService = new DefaultHashService();
      hashService.setHashAlgorithmName("SHA-512");
      hashService.setHashIterations(0);
      HashRequest request = (new HashRequest.Builder()).setSalt(salt).setSource(passworld).build();
      return hashService.computeHash(request).toHex();
   }
}
