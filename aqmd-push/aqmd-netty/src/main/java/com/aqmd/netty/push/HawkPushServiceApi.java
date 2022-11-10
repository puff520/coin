package com.aqmd.netty.push;

import com.google.protobuf.MessageLite;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import java.util.Map;
import java.util.Set;

public interface HawkPushServiceApi {
   void pushMsg(Set<Channel> channels, short cmd, String msg);

   void pushMsg(Set<Channel> channels, short cmd, byte[] msg);

   void pushMsg(Set<Channel> channels, short cmd, MessageLite msg);

   Map<String, ChannelPromise> syncPushMsg(Set<Channel> channels, short cmd, String msg);

   Map<String, ChannelPromise> syncPushMsg(Set<Channel> channels, short cmd, byte[] msg);

   Map<String, ChannelPromise> syncPushMsg(Set<Channel> channels, short cmd, MessageLite msg);
}
