package com.aqmd.netty.codec;

import io.netty.channel.Channel;

public interface Codec {
   byte[] decrypt(Channel channel, byte[] body);

   byte[] encrypt(Channel channel, byte[] body);
}
