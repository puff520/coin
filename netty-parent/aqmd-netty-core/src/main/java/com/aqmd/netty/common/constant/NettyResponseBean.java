package com.aqmd.netty.common.constant;

public class NettyResponseBean {
   private int responseCode;
   private String responseMessage;

   public NettyResponseBean(int responseCode, String responseMessage) {
      this.responseCode = responseCode;
      this.responseMessage = responseMessage;
   }

   public int getResponseCode() {
      return this.responseCode;
   }

   public void setResponseCode(int responseCode) {
      this.responseCode = responseCode;
   }

   public String getResponseMessage() {
      return this.responseMessage;
   }

   public void setResponseMessage(String responseMessage) {
      this.responseMessage = responseMessage;
   }

   public String getResponseString() {
      return this.responseCode + "~" + this.responseMessage;
   }
}
