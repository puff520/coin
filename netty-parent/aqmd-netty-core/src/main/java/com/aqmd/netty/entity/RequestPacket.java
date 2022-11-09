package com.aqmd.netty.entity;

public class RequestPacket extends Packet {
   protected static final int HEADER_LENGTH = 26;
   private int version;
   private String terminalType;

   public int getVersion() {
      return this.version;
   }

   public void setVersion(int version) {
      this.version = version;
   }

   public String getTerminalType() {
      return this.terminalType;
   }

   public void setTerminalType(String terminalType) {
      this.terminalType = terminalType;
   }

   public int getHeaderLength() {
      return 26;
   }
}
