package com.aqmd.netty.server;

import com.aqmd.netty.Dispatcher;
import com.aqmd.netty.configuration.NettyProperties;
import com.aqmd.netty.entity.Packet;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelPromise;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HandlerThreadDispatcher {
   protected final Logger logger = LoggerFactory.getLogger(this.getClass());
   private ExecutorService executor;

   @Autowired
   public HandlerThreadDispatcher(NettyProperties nettyProperties) {
      this.executor = Executors.newFixedThreadPool(nettyProperties.getDealHandlerThreadSize());
   }

   public void runByThread(ChannelHandlerContext ctx, RequestPacket msg, Dispatcher<RequestPacket, ResponsePacket> dispatcher) {
      try {
         HandlerBusinessDealThread thread = new HandlerBusinessDealThread(ctx, msg, dispatcher);
         this.executor.submit(thread);
      } catch (Exception var5) {
         this.logger.error(var5.getMessage(), var5);
      }

   }

   public class HandlerBusinessDealThread implements Runnable {
      private Dispatcher<RequestPacket, ResponsePacket> dispatcher;
      private ChannelHandlerContext ctx;
      private RequestPacket packet;

      private HandlerBusinessDealThread(ChannelHandlerContext ctx, RequestPacket packet, Dispatcher<RequestPacket, ResponsePacket> dispatcher) {
         this.ctx = ctx;
         this.packet = packet;
         this.dispatcher = dispatcher;
      }

      public void run() {
         Packet response = this.dispatcher.dispatch(this.packet, this.ctx);
         if (this.packet.getCmd() != 11004 && response != null) {
            this.ctx.writeAndFlush(response, (new DefaultChannelPromise(this.ctx.channel())).addListener((channelFuture) -> {
               this.responseComplete(this.packet);
            }));
         }

      }

      private void responseComplete(RequestPacket packet) {
         HandlerThreadDispatcher.this.logger.info("Respone the request of seqId={},cmd={}", packet.getSequenceId(), packet.getCmd());
      }

      // $FF: synthetic method
      HandlerBusinessDealThread(ChannelHandlerContext x1, RequestPacket x2, Dispatcher x3, Object x4) {
         this(x1, x2, x3);
      }
   }
}
