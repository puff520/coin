package com.aqmd.netty.common;

public enum ServerStatus {
   DEFAULT(0),
   INIT(1),
   ALIVE(2),
   DEAD(3),
   CLOSE(4);

   public final int value;

   private ServerStatus(int value) {
      this.value = value;
   }

   public boolean isClose() {
      return this == CLOSE;
   }

   public boolean isAlive() {
      return this == ALIVE;
   }
}
