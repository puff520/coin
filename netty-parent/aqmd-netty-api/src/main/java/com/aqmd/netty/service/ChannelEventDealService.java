package com.aqmd.netty.service;

public interface ChannelEventDealService {
   void dealChannelActive(String serverIp, String clientIp, int clientPort);

   void dealChannelDestory(String serverIp, String clientIp, int clientPort);
}
