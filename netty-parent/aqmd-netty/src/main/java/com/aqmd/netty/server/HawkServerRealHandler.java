package com.aqmd.netty.server;

import com.aqmd.netty.common.NettyCacheUtils;
import com.aqmd.netty.service.ChannelEventDealService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.net.InetSocketAddress;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@Sharable
public class HawkServerRealHandler extends HawkServerHandler {
   private static final Logger logger = LoggerFactory.getLogger(HawkServerRealHandler.class);
   @Autowired
   private ChannelEventDealService channelEventDealService;

   public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      super.userEventTriggered(ctx, evt);
      if (evt instanceof IdleStateEvent) {
         IdleStateEvent event = (IdleStateEvent)evt;
         if (IdleState.READER_IDLE == event.state()) {
            logger.info("id:0x{}，读空闲", ctx.channel().id().asLongText());
         } else if (IdleState.WRITER_IDLE == event.state()) {
            logger.info("id:0x{}，写空闲", ctx.channel().id().asLongText());
         } else if (IdleState.ALL_IDLE == event.state()) {
            logger.info("id:0x{}，读写空闲", ctx.channel().id().asLongText());
            ctx.close((new DefaultChannelPromise(ctx.channel())).addListener((channelFuture) -> {
               logger.error("timeoutClose");
               logger.info("channel[0x{}] timeout closed", ctx.channel().id().asLongText());
            }));
         }
      }

   }

   public void channelActive(ChannelHandlerContext ctx) {
      try {
         InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
         InetSocketAddress localAddress = (InetSocketAddress)ctx.channel().localAddress();
         logger.info("channel[{}] from {}:{} actived.", new Object[]{ctx.channel().id().asLongText(), remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort()});
         this.channelEventDealService.dealChannelActive(localAddress.getAddress().getHostAddress(), remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void handlerRemoved(ChannelHandlerContext ctx) {
      try {
         InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
         InetSocketAddress localAddress = (InetSocketAddress)ctx.channel().localAddress();
         Channel channel = ctx.channel();
         logger.info("channel[{}] from {}:{} disconnected.", new Object[]{ctx.channel().id().asLongText(), remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort()});
         String user = (String)NettyCacheUtils.keyChannelCache.get(channel);
         if (user != null) {
            logger.info("remove the push request from memeory");
            Set<Channel> channels = NettyCacheUtils.getChannel(user);
            if (!CollectionUtils.isEmpty(channels)) {
               boolean flag = channels.remove(channel);
               logger.info("user[{}] channel remove :" + flag, user);
            }

            Set<String> keys = (Set)NettyCacheUtils.userKey.get(user);
            if (!CollectionUtils.isEmpty(keys)) {
               logger.info("need remove keys,total[{}]", keys.size());
               keys.forEach((key) -> {
                  Set<Channel> keyChannels = NettyCacheUtils.getChannel(key);
                  if (!CollectionUtils.isEmpty(keyChannels)) {
                     boolean flag = keyChannels.remove(channel);
                     logger.debug("key[{}] channel remove :" + flag, key);
                  }

               });
            }
         }

         this.channelEventDealService.dealChannelDestory(localAddress.getAddress().getHostAddress(), remoteAddress.getAddress().getHostAddress(), remoteAddress.getPort());
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static int writeAndFlush(String username, short cmd, byte[] body) {
      return 1;
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      ctx.close((new DefaultChannelPromise(ctx.channel())).addListener((channelFuture) -> {
         logger.error("exception-" + cause.getMessage() + "导致的channel关闭");
      }));
   }
}
