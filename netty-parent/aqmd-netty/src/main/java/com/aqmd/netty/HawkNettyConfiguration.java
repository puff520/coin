package com.aqmd.netty;

import com.aqmd.netty.codec.DefaultCodec;
import com.aqmd.netty.configuration.NettyProperties;
import com.aqmd.netty.context.HawkContext;
import com.aqmd.netty.core.common.NettySpringContextUtils;
import com.aqmd.netty.dispatcher.HawkRequestDispatcher;
import com.aqmd.netty.handler.HeartBeatHandler;
import com.aqmd.netty.push.HawkPushServiceApi;
import com.aqmd.netty.push.impl.HawkPushServiceImpl;
import com.aqmd.netty.server.*;
import com.aqmd.netty.service.ChannelEventDealService;
import com.aqmd.netty.service.DefaultChannelEventDealService;
import com.aqmd.netty.service.DefaultLoginUserService;
import com.aqmd.netty.service.LoginUserService;
import com.aqmd.netty.websocket.WebSocketChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HawkNettyConfiguration {
    @Bean
    public NettyProperties nettyProperties() {
        return new NettyProperties();
    }


    @Bean
    public DefaultCodec codec() {
        return new DefaultCodec();
    }

    @Bean
    public HawkRequestDispatcher dispatcher() {
        return new HawkRequestDispatcher();
    }

    @Bean
    public HandlerThreadDispatcher threadDispatcher(NettyProperties nettyProperties) {
        return new HandlerThreadDispatcher(nettyProperties);
    }

    @Bean
    public HawkContext hawkContext() {
        return new HawkContext();
    }

    @Bean
    @ConditionalOnMissingBean({HawkServerHandler.class})
    public HawkServerRealHandler hawkServerRealHandler() {
        return new HawkServerRealHandler();
    }

    @Bean
    public ChannelInitializer<SocketChannel> hawkServerInitializer() {
        return new HawkServerInitializer();
    }

    @Bean
    public ChannelInitializer<SocketChannel> webSocketChannelInitializer() {
        return new WebSocketChannelInitializer();
    }

    @Bean
    public NettyApplicationStartup nettyApplicationStartup() {
        return new NettyApplicationStartup();
    }

    @Bean
    @ConditionalOnMissingBean({LoginUserService.class})
    public LoginUserService loginUserService() {
        return new DefaultLoginUserService();
    }

    @Bean
    @ConditionalOnMissingBean({ChannelEventDealService.class})
    public ChannelEventDealService channelEventDealService() {
        return new DefaultChannelEventDealService();
    }

    @Bean
    public NettySpringContextUtils nettySpringContextUtils() {
        return new NettySpringContextUtils();
    }

    @Bean
    public HawkPushServiceApi hawkPushServiceApi() {
        return new HawkPushServiceImpl();
    }


    @Bean
    public HeartBeatHandler heartBeatHandler() {
        return new HeartBeatHandler();
    }



}
