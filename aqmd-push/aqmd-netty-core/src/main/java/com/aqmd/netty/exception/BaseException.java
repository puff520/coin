package com.aqmd.netty.exception;

public class BaseException extends RuntimeException {
   private static final long serialVersionUID = 1381325479896057076L;
   private String code;
   private Object[] values;

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public Object[] getValues() {
      return this.values;
   }

   public void setValues(Object[] values) {
      this.values = values;
   }

   public BaseException(String message, Throwable cause, String code, Object[] values) {
      super(message, cause);
      this.code = code;
      this.values = values;
   }

   public BaseException(Throwable cause) {
      super(cause);
   }
}
