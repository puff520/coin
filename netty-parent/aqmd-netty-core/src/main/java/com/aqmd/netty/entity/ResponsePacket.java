package com.aqmd.netty.entity;

public class ResponsePacket extends Packet {
   protected static final int HEADER_LENGTH = 22;
   private int code;

   public int getCode() {
      return this.code;
   }

   public void setCode(int code) {
      this.code = code;
   }

   public int getHeaderLength() {
      return 22;
   }
}
