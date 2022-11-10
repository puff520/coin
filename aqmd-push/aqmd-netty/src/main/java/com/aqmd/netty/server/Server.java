package com.aqmd.netty.server;

public interface Server {
   void open();

   void close();

   boolean isClosed();

   boolean isAvailable();
}
