package com.aqmd.netty;

import com.aqmd.netty.codec.DefaultCodec;
import com.aqmd.netty.configuration.NettyProperties;
import com.aqmd.netty.context.HawkContext;
import com.aqmd.netty.core.common.NettySpringContextUtils;
import com.aqmd.netty.dispatcher.HawkRequestDispatcher;
import com.aqmd.netty.filter.AccessAuthFilter;
import com.aqmd.netty.filter.DelegatingHawkFilterProxy;
import com.aqmd.netty.handler.HeartBeatHandler;
import com.aqmd.netty.handler.JsonLoginHandler;
import com.aqmd.netty.handler.LoginHandler;
import com.aqmd.netty.push.HawkPushServiceApi;
import com.aqmd.netty.push.impl.HawkPushServiceImpl;
import com.aqmd.netty.server.HandlerThreadDispatcher;
import com.aqmd.netty.server.HawkServerHandler;
import com.aqmd.netty.server.HawkServerInitializer;
import com.aqmd.netty.server.HawkServerRealHandler;
import com.aqmd.netty.server.NettyApplicationStartup;
import com.aqmd.netty.service.ChannelEventDealService;
import com.aqmd.netty.service.DefaultChannelEventDealService;
import com.aqmd.netty.service.DefaultLoginUserService;
import com.aqmd.netty.service.LoginUserService;
import com.aqmd.netty.shiro.HawkShiroFilterFactoryBean;
import com.aqmd.netty.shiro.SequenceSessionIdGenerator;
import com.aqmd.netty.shiro.cache.SpringCacheManagerWrapper;
import com.aqmd.netty.shiro.mgt.DefaultHawkSecurityManager;
import com.aqmd.netty.shiro.mgt.DefaultHawkSubjectFactory;
import com.aqmd.netty.shiro.realm.HawkServerRealm;
import com.aqmd.netty.shiro.session.DefaultHawkSessionManager;
import com.aqmd.netty.websocket.WebSocketChannelInitializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class HawkNettyConfiguration {
   @Bean
   public NettyProperties nettyProperties() {
      return new NettyProperties();
   }

   @Bean
   @ConditionalOnMissingBean({Realm.class})
   public Realm realm() {
      return new HawkServerRealm();
   }

   @Bean
   public SequenceSessionIdGenerator sessionIdGenerator() {
      return new SequenceSessionIdGenerator();
   }

   @Bean
   public EnterpriseCacheSessionDAO sessionDAO(SequenceSessionIdGenerator sessionIdGenerator) {
      EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
      sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
      sessionDAO.setSessionIdGenerator(sessionIdGenerator);
      return sessionDAO;
   }

   @Bean
   public QuartzSessionValidationScheduler sessionValidationScheduler() {
      QuartzSessionValidationScheduler sessionValidationScheduler = new QuartzSessionValidationScheduler();
      sessionValidationScheduler.setSessionValidationInterval(1800000L);
      return sessionValidationScheduler;
   }

   @Bean
   public DefaultHawkSessionManager sessionManager(CachingSessionDAO sessionDAO) {
      DefaultHawkSessionManager defaultSessionManager = new DefaultHawkSessionManager();
      defaultSessionManager.setGlobalSessionTimeout(1800000L);
      defaultSessionManager.setDeleteInvalidSessions(true);
      defaultSessionManager.setSessionDAO(sessionDAO);
      return defaultSessionManager;
   }

   @Bean
   public DefaultHawkSubjectFactory hawkSubjectFactory() {
      return new DefaultHawkSubjectFactory();
   }

   @Bean
   public DefaultHawkSecurityManager securityManager(Realm realm, DefaultHawkSessionManager sessionManager, SpringCacheManagerWrapper springCacheManagerWrapper, DefaultHawkSubjectFactory hawkSubjectFactory) {
      DefaultHawkSecurityManager securityManager = new DefaultHawkSecurityManager();
      securityManager.setRealm(realm);
      securityManager.setSessionManager(sessionManager);
      securityManager.setCacheManager(springCacheManagerWrapper);
      securityManager.setSubjectFactory(hawkSubjectFactory);
      return securityManager;
   }

   @Bean
   public SpringCacheManagerWrapper springCacheManagerWrapper(EhCacheCacheManager cacheManager) {
      SpringCacheManagerWrapper spingCacheManager = new SpringCacheManagerWrapper();
      spingCacheManager.setCacheManager(cacheManager);
      return spingCacheManager;
   }

   @Bean
   public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultHawkSecurityManager securityManager) {
      MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
      methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
      methodInvokingFactoryBean.setArguments(new Object[]{securityManager});
      return methodInvokingFactoryBean;
   }

   @Bean
   public HawkShiroFilterFactoryBean hawkShiroFilter(DefaultHawkSecurityManager securityManager) {
      HawkShiroFilterFactoryBean hawkShiroFilter = new HawkShiroFilterFactoryBean();
      hawkShiroFilter.setSecurityManager(securityManager);
      return hawkShiroFilter;
   }

   @Bean
   public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
      return new LifecycleBeanPostProcessor();
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
   public LoginHandler loginHandler() {
      return new LoginHandler();
   }

   @Bean
   public JsonLoginHandler jsonLoginHandler() {
      return new JsonLoginHandler();
   }

   @Bean
   public HeartBeatHandler heartBeatHandler() {
      return new HeartBeatHandler();
   }

   @Bean
   public AccessAuthFilter accessAuthFilter() {
      return new AccessAuthFilter();
   }

   @Bean
   public DelegatingHawkFilterProxy delegatingHawkFilterProxy() {
      return new DelegatingHawkFilterProxy();
   }
}
