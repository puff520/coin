package com.aqmd.netty.entity;

public abstract class Packet {
   protected static final int MIN_LENGTH = 18;
   private int length;
   private long sequenceId;
   private int requestId;
   private short cmd;
   private byte[] body;

   public abstract int getHeaderLength();

   public int getLength() {
      return this.length == 0 ? this.getHeaderLength() : this.length;
   }

   public void setLength(int length) {
      this.length = length;
   }

   public short getCmd() {
      return this.cmd;
   }

   public void setCmd(short cmd) {
      this.cmd = cmd;
   }

   public byte[] getBody() {
      return this.body;
   }

   public int getRequestId() {
      return this.requestId;
   }

   public void setRequestId(int requestId) {
      this.requestId = requestId;
   }

   public void setBody(byte[] body) {
      this.body = body;
      if (this.body == null) {
         this.length = this.getHeaderLength();
      } else {
         this.length = this.getHeaderLength() + this.body.length;
      }

   }

   public long getSequenceId() {
      return this.sequenceId;
   }

   public void setSequenceId(long sequenceId) {
      this.sequenceId = sequenceId;
   }
}
