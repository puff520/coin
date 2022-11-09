package com.aqmd.netty.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
   prefix = "aqmd.netty"
)
public class NettyProperties {
   private int workerThreadSize;
   private int maxTimeout;
   private int defaultTimeout;
   private int dealHandlerThreadSize;
   private String serviceLoggerLevel;
   private int port;
   private int bossThreadSize;
   private int packetHeaderLength;
   private int maxFrameLength;
   private int readerIdle;
   private int writerIdle;
   private int bothIdle;
   private int maxTimeoutInterval;
   private int websocketFlag;
   private int websocketPort;
   private int directAccessFlag;
   private String directAccessCommand;

   public int getWorkerThreadSize() {
      return this.workerThreadSize;
   }

   public void setWorkerThreadSize(int workerThreadSize) {
      this.workerThreadSize = workerThreadSize;
   }

   public int getMaxTimeout() {
      return this.maxTimeout;
   }

   public void setMaxTimeout(int maxTimeout) {
      this.maxTimeout = maxTimeout;
   }

   public int getDefaultTimeout() {
      return this.defaultTimeout;
   }

   public void setDefaultTimeout(int defaultTimeout) {
      this.defaultTimeout = defaultTimeout;
   }

   public int getDealHandlerThreadSize() {
      return this.dealHandlerThreadSize;
   }

   public void setDealHandlerThreadSize(int dealHandlerThreadSize) {
      this.dealHandlerThreadSize = dealHandlerThreadSize;
   }

   public String getServiceLoggerLevel() {
      return this.serviceLoggerLevel;
   }

   public void setServiceLoggerLevel(String serviceLoggerLevel) {
      this.serviceLoggerLevel = serviceLoggerLevel;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public int getBossThreadSize() {
      return this.bossThreadSize;
   }

   public void setBossThreadSize(int bossThreadSize) {
      this.bossThreadSize = bossThreadSize;
   }

   public int getPacketHeaderLength() {
      return this.packetHeaderLength;
   }

   public void setPacketHeaderLength(int packetHeaderLength) {
      this.packetHeaderLength = packetHeaderLength;
   }

   public int getMaxFrameLength() {
      return this.maxFrameLength;
   }

   public void setMaxFrameLength(int maxFrameLength) {
      this.maxFrameLength = maxFrameLength;
   }

   public int getReaderIdle() {
      return this.readerIdle;
   }

   public void setReaderIdle(int readerIdle) {
      this.readerIdle = readerIdle;
   }

   public int getWriterIdle() {
      return this.writerIdle;
   }

   public void setWriterIdle(int writerIdle) {
      this.writerIdle = writerIdle;
   }

   public int getBothIdle() {
      return this.bothIdle;
   }

   public void setBothIdle(int bothIdle) {
      this.bothIdle = bothIdle;
   }

   public int getMaxTimeoutInterval() {
      return this.maxTimeoutInterval;
   }

   public void setMaxTimeoutInterval(int maxTimeoutInterval) {
      this.maxTimeoutInterval = maxTimeoutInterval;
   }

   public int getWebsocketFlag() {
      return this.websocketFlag;
   }

   public void setWebsocketFlag(int websocketFlag) {
      this.websocketFlag = websocketFlag;
   }

   public int getWebsocketPort() {
      return this.websocketPort;
   }

   public void setWebsocketPort(int websocketPort) {
      this.websocketPort = websocketPort;
   }

   public int getDirectAccessFlag() {
      return this.directAccessFlag;
   }

   public void setDirectAccessFlag(int directAccessFlag) {
      this.directAccessFlag = directAccessFlag;
   }

   public String getDirectAccessCommand() {
      return this.directAccessCommand;
   }

   public void setDirectAccessCommand(String directAccessCommand) {
      this.directAccessCommand = directAccessCommand;
   }
}
