package com.aqmd.netty.push.impl;

import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.push.HawkPushServiceApi;
import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class HawkPushServiceImpl implements HawkPushServiceApi {
   protected final Logger logger = LoggerFactory.getLogger(this.getClass());
   private static final int MIN_SEQ_ID = 536870911;
   private static AtomicInteger idWoker = new AtomicInteger(536870911);

   public void pushMsg(Set<Channel> channels, short cmd, String msg) {
      if (!CollectionUtils.isEmpty(channels)) {
         Iterator<Channel> iterable = channels.iterator();

         while(iterable.hasNext()) {
            Channel channel = (Channel)iterable.next();

            try {
               if (channel != null && channel.isActive()) {
                  channel.writeAndFlush(this.buildResponsePacket(cmd, msg.getBytes()));
               } else {
                  this.logger.debug("推送通道被关闭，移除该推送通道");
                  iterable.remove();
                  this.logger.debug("通道移除结果:true");
               }
            } catch (Exception var7) {
               this.logger.error(var7.getMessage(), var7);
            }
         }

      }
   }

   public void pushMsg(Set<Channel> channels, short cmd, byte[] msg) {
      if (channels != null && !CollectionUtils.isEmpty(channels)) {
         try {
            Iterator<Channel> iterable = channels.iterator();

            while(true) {
               while(iterable.hasNext()) {
                  Channel channel = (Channel)iterable.next();
                  if (channel != null && channel.isActive()) {
                     channel.writeAndFlush(this.buildResponsePacket(cmd, msg));
                  } else {
                     this.logger.debug("推送通道被关闭，移除该推送通道");
                     iterable.remove();
                     this.logger.debug("通道移除结果:true");
                  }
               }

               return;
            }
         } catch (Exception var6) {
            this.logger.error(var6.getMessage(), var6);
         }
      }
   }

   public void pushMsg(Set<Channel> channels, short cmd, MessageLite msg) {
      if (channels != null && !CollectionUtils.isEmpty(channels)) {
         try {
            Iterator<Channel> iterable = channels.iterator();

            while(true) {
               while(iterable.hasNext()) {
                  Channel channel = (Channel)iterable.next();
                  if (channel != null && channel.isActive()) {
                     channel.writeAndFlush(this.buildResponsePacket(cmd, msg.toByteArray()));
                  } else {
                     this.logger.debug("推送通道被关闭，移除该推送通道");
                     iterable.remove();
                     this.logger.debug("通道移除结果:true");
                  }
               }

               return;
            }
         } catch (Exception var6) {
            this.logger.error(var6.getMessage(), var6);
         }
      }
   }

   public Map<String, ChannelPromise> syncPushMsg(Set<Channel> channels, short cmd, String msg) {
      if (CollectionUtils.isEmpty(channels)) {
         return null;
      } else {
         Map<String, ChannelPromise> channelPromiseMap = new HashMap();
         Iterator<Channel> iterable = channels.iterator();

         while(iterable.hasNext()) {
            Channel channel = (Channel)iterable.next();

            try {
               if (channel != null && channel.isActive()) {
                  channelPromiseMap.put(channel.id().asLongText(), channel.writeAndFlush(this.buildResponsePacket(cmd, msg.getBytes())).channel().newPromise());
               } else {
                  this.logger.debug("推送通道被关闭，移除该推送通道");
                  iterable.remove();
                  this.logger.debug("通道移除结果:true");
               }
            } catch (Exception var8) {
               this.logger.error(var8.getMessage(), var8);
            }
         }

         return channelPromiseMap;
      }
   }

   public Map<String, ChannelPromise> syncPushMsg(Set<Channel> channels, short cmd, byte[] msg) {
      if (CollectionUtils.isEmpty(channels)) {
         return null;
      } else {
         Map<String, ChannelPromise> channelPromiseMap = new HashMap();
         Iterator<Channel> iterable = channels.iterator();

         while(iterable.hasNext()) {
            Channel channel = (Channel)iterable.next();

            try {
               if (channel != null && channel.isActive()) {
                  channelPromiseMap.put(channel.id().asLongText(), channel.writeAndFlush(this.buildResponsePacket(cmd, msg)).channel().newPromise());
               } else {
                  this.logger.debug("推送通道被关闭，移除该推送通道");
                  iterable.remove();
                  this.logger.debug("通道移除结果:true");
               }
            } catch (Exception var8) {
               this.logger.error(var8.getMessage(), var8);
            }
         }

         return channelPromiseMap;
      }
   }

   public Map<String, ChannelPromise> syncPushMsg(Set<Channel> channels, short cmd, MessageLite msg) {
      if (CollectionUtils.isEmpty(channels)) {
         return null;
      } else {
         Map<String, ChannelPromise> channelPromiseMap = new HashMap();
         Iterator<Channel> iterable = channels.iterator();

         while(iterable.hasNext()) {
            Channel channel = (Channel)iterable.next();

            try {
               if (channel != null && channel.isActive()) {
                  channelPromiseMap.put(channel.id().asLongText(), channel.writeAndFlush(this.buildResponsePacket(cmd, msg.toByteArray())).channel().newPromise());
               } else {
                  this.logger.debug("推送通道被关闭，移除该推送通道");
                  iterable.remove();
                  this.logger.debug("通道移除结果:true");
               }
            } catch (Exception var8) {
               this.logger.error(var8.getMessage(), var8);
            }
         }

         return channelPromiseMap;
      }
   }

   private ResponsePacket buildResponsePacket(short cmd, byte[] body) {
      ResponsePacket packet = new ResponsePacket();
      packet.setCmd(cmd);
      packet.setSequenceId((long)nextSeqId());
      packet.setCode(NettyResponseCode.SUCCESS.getResponseCode());
      packet.setBody(body);
      return packet;
   }

   private static int nextSeqId() {
      int seqId;
      for(seqId = idWoker.getAndIncrement(); seqId < 536870911; seqId = idWoker.addAndGet(536870911)) {
      }

      return seqId;
   }
}
