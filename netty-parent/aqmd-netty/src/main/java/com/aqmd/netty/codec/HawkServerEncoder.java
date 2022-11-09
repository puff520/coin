package com.aqmd.netty.codec;

import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HawkServerEncoder extends MessageToByteEncoder<ResponsePacket> {
   private static final Logger LOGGER = LoggerFactory.getLogger(HawkServerEncoder.class);
   private Codec codec;

   public HawkServerEncoder() {
      this(new DefaultCodec());
   }

   public HawkServerEncoder(Codec codec) {
      this.codec = codec;
   }

   protected void encode(ChannelHandlerContext ctx, ResponsePacket packet, ByteBuf out) throws NettyException {
      LOGGER.debug("原始包长度:{}", packet.getLength());
      byte[] body = this.codec.encrypt(ctx.channel(), packet.getBody());
      packet.setBody(body);
      LOGGER.debug("加密后包长度:{}", packet.getLength());
      out.writeInt(packet.getLength());
      out.writeLong(packet.getSequenceId());
      out.writeShort(packet.getCmd());
      out.writeInt(packet.getCode());
      out.writeInt(packet.getRequestId());
      if (body != null) {
         out.writeBytes(body);
      }

   }
}
