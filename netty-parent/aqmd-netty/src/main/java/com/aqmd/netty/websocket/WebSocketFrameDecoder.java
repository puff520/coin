package com.aqmd.netty.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;

public class WebSocketFrameDecoder extends MessageToMessageDecoder<WebSocketFrame> {
   protected void decode(ChannelHandlerContext ctx, WebSocketFrame msg, List<Object> out) {
      ByteBuf buff = msg.content();
      byte[] messageBytes = new byte[buff.readableBytes()];
      buff.readBytes(messageBytes);
      ByteBuf bytebuf = PooledByteBufAllocator.DEFAULT.buffer();
      bytebuf.writeBytes(messageBytes);
      out.add(bytebuf.retain());
   }
}
