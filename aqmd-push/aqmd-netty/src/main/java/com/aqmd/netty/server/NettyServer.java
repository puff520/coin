package com.aqmd.netty.server;

import com.aqmd.netty.exception.NettyException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer implements Server, Runnable {
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   private int port;
   private int bossThreadSize;
   private int workerThreadSize;
   private ChannelInitializer channelInitializer;

   NettyServer(int port, int bossThreadSize, int workerThreadSize, ChannelInitializer channelInitializer) {
      this.port = port;
      this.bossThreadSize = bossThreadSize;
      this.workerThreadSize = workerThreadSize;
      this.channelInitializer = channelInitializer;
   }

   public void open() {
      NioEventLoopGroup bossGroup = new NioEventLoopGroup(this.bossThreadSize);
      NioEventLoopGroup workerGroup = new NioEventLoopGroup(this.workerThreadSize);
      ServerBootstrap b = new ServerBootstrap();
      ((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)((ServerBootstrap)b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)).option(ChannelOption.SO_BACKLOG, 1024)).option(ChannelOption.SO_RCVBUF, 1048576)).childOption(ChannelOption.SO_SNDBUF, 10485760).childOption(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)).childOption(ChannelOption.TCP_NODELAY, true).childHandler(this.channelInitializer);
      InetSocketAddress localAddress = new InetSocketAddress(this.port);

      try {
         ChannelFuture f = b.bind(localAddress).sync();
         this.logger.info("Server started at port {}", localAddress.getPort());
         Channel serverChannel = f.channel();
         serverChannel.closeFuture().sync();
      } catch (InterruptedException var10) {
         throw new NettyException(var10.getMessage());
      } finally {
         bossGroup.shutdownGracefully();
         workerGroup.shutdownGracefully();
      }

   }

   public void run() {
      this.open();
   }

   public void close() {
   }

   public boolean isClosed() {
      return false;
   }

   public boolean isAvailable() {
      return false;
   }
}
