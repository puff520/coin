package com.aqmd.netty.annotation;

public class HawkMethodValue {
   private int cmd;
   private byte version;
   private boolean obsoleted;

   public HawkMethodValue() {
   }

   public HawkMethodValue(int cmd, byte version, boolean obsoleted) {
      this.cmd = cmd;
      this.version = version;
      this.obsoleted = obsoleted;
   }

   public int getCmd() {
      return this.cmd;
   }

   public void setCmd(int cmd) {
      this.cmd = cmd;
   }

   public byte getVersion() {
      return this.version;
   }

   public void setVersion(byte version) {
      this.version = version;
   }

   public boolean isObsoleted() {
      return this.obsoleted;
   }

   public void setObsoleted(boolean obsoleted) {
      this.obsoleted = obsoleted;
   }
}
