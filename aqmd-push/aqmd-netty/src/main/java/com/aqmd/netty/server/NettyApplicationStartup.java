package com.aqmd.netty.server;

import com.aqmd.netty.configuration.NettyProperties;
import com.aqmd.netty.websocket.WebSocketChannelInitializer;
import io.netty.channel.ChannelInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class NettyApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
   public void onApplicationEvent(ContextRefreshedEvent event) {
      NettyProperties nettyProperties = (NettyProperties)event.getApplicationContext().getBean(NettyProperties.class);
      ChannelInitializer hawkServerInitializer = (ChannelInitializer)event.getApplicationContext().getBean(HawkServerInitializer.class);
      Thread thread = new Thread(new NettyServer(nettyProperties.getPort(), nettyProperties.getBossThreadSize(), nettyProperties.getWorkerThreadSize(), hawkServerInitializer));
      thread.start();
      if (nettyProperties.getWebsocketFlag() == 1) {
         ChannelInitializer webSocketChannelInitializer = (ChannelInitializer)event.getApplicationContext().getBean(WebSocketChannelInitializer.class);
         Thread websocketThread = new Thread(new NettyServer(nettyProperties.getWebsocketPort(), nettyProperties.getBossThreadSize(), nettyProperties.getWorkerThreadSize(), webSocketChannelInitializer));
         websocketThread.start();
      }

   }
}
