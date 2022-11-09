package com.aqmd.netty.codec;

import io.netty.channel.Channel;
import java.util.Base64;

public class Base64Codec implements Codec {
   public byte[] decrypt(Channel channel, byte[] body) {
      return Base64.getDecoder().decode(body);
   }

   public byte[] encrypt(Channel channel, byte[] body) {
      return Base64.getEncoder().encode(body);
   }
}
