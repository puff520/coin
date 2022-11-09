package com.aqmd.netty.websocket;

import com.aqmd.netty.codec.Codec;
import com.aqmd.netty.codec.HawkServerDecoder;
import com.aqmd.netty.codec.HawkServerEncoder;
import com.aqmd.netty.configuration.NettyProperties;
import com.aqmd.netty.server.HawkServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
   @Autowired
   private Codec codec;
   @Autowired
   private HawkServerHandler handler;
   @Autowired
   private NettyProperties nettyProperties;

   protected void initChannel(SocketChannel ch) {
      ChannelPipeline pipeline = ch.pipeline();
      pipeline.addLast("logger", new LoggingHandler(LogLevel.WARN)).addLast("httpServerCodec", new HttpServerCodec()).addLast("chunkedWriteHandler", new ChunkedWriteHandler()).addLast("httpObjectAggregator", new HttpObjectAggregator(65536)).addLast("webSocketServerProtocolHandler", new WebSocketServerProtocolHandler("/ws")).addLast("websocketDecoder", new WebSocketFrameDecoder()).addLast("decoder", new HawkServerDecoder(this.codec)).addLast("websocketEncoder", new WebSocketFramePrepender()).addLast("encoder", new HawkServerEncoder(this.codec)).addLast("idle", new IdleStateHandler(this.nettyProperties.getReaderIdle(), this.nettyProperties.getWriterIdle(), this.nettyProperties.getBothIdle())).addLast("handler", this.handler);
   }
}
