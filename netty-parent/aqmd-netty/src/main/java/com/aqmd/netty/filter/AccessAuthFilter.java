package com.aqmd.netty.filter;

import com.aqmd.netty.annotation.HawkFilter;
import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.configuration.NettyProperties;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import io.netty.channel.ChannelHandlerContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

@HawkFilter(
   order = 2
)
public class AccessAuthFilter extends HFilter {
   @Autowired
   private NettyProperties nettyProperties;

   public void init() throws NettyException {
   }

   public void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws NettyException {
      Subject subject = SecurityUtils.getSubject();
      if (this.nettyProperties.getDirectAccessFlag() != 1 || this.nettyProperties.getDirectAccessCommand() == null || !this.nettyProperties.getDirectAccessCommand().contains(String.valueOf(request.getCmd()))) {
         Session session;
         if (request.getCmd() != 11002 && request.getCmd() != 11000 && subject.getPrincipal() == null) {
            session = subject.getSession();
            response.setSequenceId((Long)session.getId());
            throw new NettyException(this.buildExceptionMsg(NettyResponseCode.NOLOGIN_ERROR.getResponseCode(), NettyResponseCode.NOLOGIN_ERROR.getResponseMessage()));
         }

         if (request.getCmd() == 11002 || request.getCmd() == 11000) {
            subject.getPrincipal();
            session = subject.getSession();
            request.setSequenceId((Long)session.getId());
            response.setSequenceId((Long)session.getId());
         }

         chain.doFilter(request, response, ctx);
      }

   }

   public static void main(String[] args) {
   }

   public void destroy() {
   }
}
