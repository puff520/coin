package com.aqmd.netty.shiro.filter;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import com.aqmd.netty.filter.FilterChain;
import com.aqmd.netty.filter.HFilter;
import com.aqmd.netty.shiro.subject.HawkSubject;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractShiroFilter extends HFilter {
   private static final Logger log = LoggerFactory.getLogger(AbstractShiroFilter.class);
   private SecurityManager securityManager;

   protected Subject createSubject(RequestPacket request, ResponsePacket response) {
      return (new HawkSubject.Builder(this.getSecurityManager(), request, response)).buildHawkSubject();
   }

   protected void updateSessionLastAccessTime(RequestPacket request, ResponsePacket response) {
      Subject subject = SecurityUtils.getSubject();
      if (subject != null) {
         ThreadContext.bind(subject);
         Session session = subject.getSession(false);
         if (session != null) {
            try {
               session.touch();
            } catch (Throwable var6) {
               log.error("session.touch() method invocation has failed.  Unable to updatethe corresponding session's last access time based on the incoming request.", var6);
            }
         }
      }

   }

   protected void doFilterInternal(final RequestPacket request, final ResponsePacket response, final ChannelHandlerContext ctx, final FilterChain chain) throws NettyException, IOException {
      Throwable t = null;

      try {
         Subject subject = this.createSubject(request, response);
         subject.execute(new Callable() {
            public Object call() throws Exception {
               AbstractShiroFilter.this.updateSessionLastAccessTime(request, response);
               chain.doFilter(request, response, ctx);
               return null;
            }
         });
      } catch (ExecutionException var7) {
         t = var7.getCause();
      } catch (Throwable var8) {
         t = var8;
      }

      if (t != null) {
         if (t instanceof NettyException) {
            throw (NettyException)t;
         } else if (t instanceof IOException) {
            throw (IOException)t;
         } else {
            log.error(t.getMessage(), t);
            String msg = "Filtered request failed.";
            throw new NettyException(t, msg);
         }
      }
   }

   public final void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws NettyException, IOException {
      this.doFilterInternal(request, response, ctx, chain);
   }

   public void init() {
   }

   public void destroy() {
   }

   public SecurityManager getSecurityManager() {
      return this.securityManager;
   }

   public void setSecurityManager(SecurityManager securityManager) {
      this.securityManager = securityManager;
   }
}
