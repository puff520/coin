package com.aqmd.netty.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HawkMethod {
   int cmd();

   byte version() default 1;

   ObsoletedType obsoleted() default ObsoletedType.NO;
}
