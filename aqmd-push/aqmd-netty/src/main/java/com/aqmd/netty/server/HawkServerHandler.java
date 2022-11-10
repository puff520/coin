package com.aqmd.netty.server;

import com.aqmd.netty.dispatcher.HawkRequestDispatcher;
import com.aqmd.netty.entity.RequestPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class HawkServerHandler extends SimpleChannelInboundHandler<RequestPacket> {
   protected final Logger logger = LoggerFactory.getLogger(this.getClass());
   @Autowired
   private HandlerThreadDispatcher threadDispatcher;
   @Autowired
   private HawkRequestDispatcher dispatcher;

   protected void channelRead0(ChannelHandlerContext ctx, RequestPacket packet) {
      InetSocketAddress remoteAddress = (InetSocketAddress)ctx.channel().remoteAddress();
      this.logger.info("Receive Request from {} of {}, cmd={}, version={}, seqId={}", new Object[]{remoteAddress.getAddress().getHostAddress(), ctx.channel().id().asLongText(), packet.getCmd(), packet.getVersion(), packet.getSequenceId()});
      this.threadDispatcher.runByThread(ctx, packet, this.dispatcher);
   }

   public void channelReadComplete(ChannelHandlerContext ctx) {
      ctx.flush();
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
      cause.printStackTrace();
      ctx.close();
   }

   public void handlerRemoved(ChannelHandlerContext ctx) {
      ctx.disconnect(ctx.newPromise());
      ctx.close();
   }

   public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      super.handlerAdded(ctx);
   }
}
