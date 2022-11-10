package com.aqmd.netty.codec;

import com.aqmd.netty.entity.RequestPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HawkServerDecoder extends ByteToMessageDecoder {
   private static final Logger LOGGER = LoggerFactory.getLogger(HawkServerDecoder.class);
   private Codec codec;

   public HawkServerDecoder() {
      this(new DefaultCodec());
   }

   public HawkServerDecoder(Codec codec) {
      this.codec = codec;
   }

   protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {
      RequestPacket packet = new RequestPacket();
      if (byteBuf != null && ctx.channel().isActive()) {
         int packetLen = byteBuf.readInt();
         LOGGER.debug("原始包长度：{}", packetLen);
         packet.setSequenceId(byteBuf.readLong());
         packet.setCmd(byteBuf.readShort());
         packet.setVersion(byteBuf.readInt());
         byte[] termByte = new byte[4];
         byteBuf.readBytes(termByte);
         packet.setTerminalType(new String(termByte));
         packet.setRequestId(byteBuf.readInt());
         byte[] tytes = new byte[byteBuf.readableBytes()];
         byteBuf.readBytes(tytes);
         packet.setBody(tytes);
         packetLen = packet.getLength();
         LOGGER.debug("解密后包长度：{}", packetLen);
         packet.setLength(packetLen);
         list.add(packet);
      }
   }
}
