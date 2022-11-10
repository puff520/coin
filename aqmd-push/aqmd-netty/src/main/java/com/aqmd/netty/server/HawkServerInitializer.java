package com.aqmd.netty.server;

import com.aqmd.netty.codec.Codec;
import com.aqmd.netty.codec.HawkServerDecoder;
import com.aqmd.netty.codec.HawkServerEncoder;
import com.aqmd.netty.configuration.NettyProperties;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class HawkServerInitializer extends ChannelInitializer<SocketChannel> {
   @Autowired
   private Codec codec;
   @Autowired
   private HawkServerHandler handler;
   @Autowired
   private NettyProperties nettyProperties;

   protected void initChannel(SocketChannel socketChannel) {
      LogLevel logLevel;
      switch (this.nettyProperties.getServiceLoggerLevel()) {
         case "debug":
            logLevel = LogLevel.DEBUG;
            break;
         case "info":
            logLevel = LogLevel.INFO;
            break;
         case "warn":
            logLevel = LogLevel.WARN;
            break;
         case "error":
            logLevel = LogLevel.ERROR;
            break;
         default:
            logLevel = LogLevel.ERROR;
      }

      socketChannel.pipeline().addLast("logger", new LoggingHandler(logLevel)).addFirst(new ChannelHandler[]{new LengthFieldBasedFrameDecoder(this.nettyProperties.getMaxFrameLength(), 0, this.nettyProperties.getPacketHeaderLength(), -this.nettyProperties.getPacketHeaderLength(), 0)}).addLast("decoder", new HawkServerDecoder(this.codec)).addLast("encoder", new HawkServerEncoder(this.codec)).addLast("idle", new IdleStateHandler(this.nettyProperties.getReaderIdle(), this.nettyProperties.getWriterIdle(), this.nettyProperties.getBothIdle())).addLast("handler", this.handler);
   }
}
