package com.aqmd.netty.filter;

import com.aqmd.netty.annotation.HawkFilter;
import com.aqmd.netty.core.common.NettySpringContextUtils;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import com.aqmd.netty.shiro.filter.AbstractShiroFilter;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;

@HawkFilter(
   order = 1
)
public class DelegatingHawkFilterProxy extends HFilter {
   protected final Logger logger;
   private ApplicationContext applicationContext;
   private String contextAttribute;
   private String targetBeanName;
   private Environment environment;
   private boolean targetFilterLifecycle;
   private final Object delegateMonitor;
   private volatile HFilter delegate;

   public DelegatingHawkFilterProxy() {
      this.logger = LoggerFactory.getLogger(this.getClass());
      this.environment = new StandardEnvironment();
      this.targetFilterLifecycle = true;
      this.delegateMonitor = new Object();
      this.init();
   }

   public void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws IOException, NettyException {
      this.doFilterInternal(request, response, ctx, chain);
   }

   public DelegatingHawkFilterProxy(HFilter delegate) {
      this.logger = LoggerFactory.getLogger(this.getClass());
      this.environment = new StandardEnvironment();
      this.targetFilterLifecycle = true;
      this.delegateMonitor = new Object();
      Assert.notNull(delegate, "delegate Filter object must not be null");
      this.delegate = delegate;
   }

   public DelegatingHawkFilterProxy(String targetBeanName) {
      this(targetBeanName, (ApplicationContext)null);
   }

   public DelegatingHawkFilterProxy(String targetBeanName, ApplicationContext ac) {
      this.logger = LoggerFactory.getLogger(this.getClass());
      this.environment = new StandardEnvironment();
      this.targetFilterLifecycle = true;
      this.delegateMonitor = new Object();
      Assert.hasText(targetBeanName, "target Filter bean name must not be null or empty");
      this.setTargetBeanName(targetBeanName);
      this.applicationContext = ac;
      if (ac != null) {
         this.setEnvironment(ac.getEnvironment());
      }

   }

   public void init() {
      this.initFilterBean();
      if (this.logger.isDebugEnabled()) {
         this.logger.debug("Filter  configured successfully");
      }

   }

   protected void initFilterBean() throws NettyException {
      synchronized(this.delegateMonitor) {
         if (this.delegate == null) {
            if (this.targetBeanName == null) {
               this.targetBeanName = "hawkShiroFilter";
            }

            ApplicationContext wac = this.findApplicationContext();
            if (wac != null) {
               this.delegate = this.initDelegate(wac);
            }
         }

      }
   }

   protected HFilter initDelegate(ApplicationContext wac) throws NettyException {
      HFilter delegate = (HFilter)wac.getBean(this.getTargetBeanName(), AbstractShiroFilter.class);
      if (this.isTargetFilterLifecycle()) {
         delegate.init();
      }

      return delegate;
   }

   protected ApplicationContext findApplicationContext() {
      if (this.applicationContext != null) {
         if (this.applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext cac = (ConfigurableApplicationContext)this.applicationContext;
            if (!cac.isActive()) {
               cac.refresh();
            }
         }

         return this.applicationContext;
      } else {
         return NettySpringContextUtils.getApplicationContext();
      }
   }

   public void doFilterInternal(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws NettyException, IOException {
      HFilter delegateToUse = this.delegate;
      if (delegateToUse == null) {
         synchronized(this.delegateMonitor) {
            if (this.delegate == null) {
               ApplicationContext wac = this.findApplicationContext();
               if (wac == null) {
                  throw new IllegalStateException("No applicationContext found: no ContextLoaderListener or DispatcherServlet registered?");
               }

               this.delegate = this.initDelegate(wac);
            }

            delegateToUse = this.delegate;
         }
      }

      this.invokeDelegate(delegateToUse, request, response, ctx, chain);
   }

   protected void invokeDelegate(HFilter delegate, RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws NettyException, IOException {
      delegate.doFilter(request, response, ctx, chain);
   }

   public void destroy() {
      HFilter delegateToUse = this.delegate;
      if (delegateToUse != null) {
         this.destroyDelegate(delegateToUse);
      }

   }

   protected void destroyDelegate(HFilter delegate) {
      if (this.isTargetFilterLifecycle()) {
         delegate.destroy();
      }

   }

   public String getContextAttribute() {
      return this.contextAttribute;
   }

   public void setContextAttribute(String contextAttribute) {
      this.contextAttribute = contextAttribute;
   }

   public ApplicationContext getApplicationContext() {
      return this.applicationContext;
   }

   public void setApplicationContext(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
   }

   public String getTargetBeanName() {
      return this.targetBeanName;
   }

   public void setTargetBeanName(String targetBeanName) {
      this.targetBeanName = targetBeanName;
   }

   public Environment getEnvironment() {
      return this.environment;
   }

   public void setEnvironment(Environment environment) {
      this.environment = environment;
   }

   public boolean isTargetFilterLifecycle() {
      return this.targetFilterLifecycle;
   }

   public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
      this.targetFilterLifecycle = targetFilterLifecycle;
   }

   public HFilter getDelegate() {
      return this.delegate;
   }

   public void setDelegate(HFilter delegate) {
      this.delegate = delegate;
   }

   public Object getDelegateMonitor() {
      return this.delegateMonitor;
   }
}
