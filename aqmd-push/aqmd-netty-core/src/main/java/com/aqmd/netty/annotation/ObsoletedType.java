package com.aqmd.netty.annotation;

public enum ObsoletedType {
   YES,
   NO;

   public static boolean isObsoleted(ObsoletedType type) {
      return YES == type;
   }
}
