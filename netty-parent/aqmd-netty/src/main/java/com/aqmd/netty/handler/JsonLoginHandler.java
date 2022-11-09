package com.aqmd.netty.handler;

import com.alibaba.fastjson.JSON;
import com.aqmd.netty.annotation.HawkBean;
import com.aqmd.netty.annotation.HawkMethod;
import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.exception.NettyException;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import java.util.Objects;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@HawkBean
public class JsonLoginHandler {
   protected final Logger logger = LoggerFactory.getLogger(this.getClass());

   @HawkMethod(
      cmd = 11000,
      version = 1
   )
   public String login(long seqId, byte[] body, ChannelHandlerContext ctx) {
      Subject subject = SecurityUtils.getSubject();

      try {
         Map<String, String> user = (Map)JSON.parse(new String(body));
         UsernamePasswordToken token = new UsernamePasswordToken((String)user.get("username"), (String)user.get("password"));
         subject.login(token);
      } catch (IncorrectCredentialsException | UnknownAccountException var10) {
         throw new NettyException(var10, NettyResponseCode.LOGIN_AUTH_ERROR.getResponseCode() + "~" + NettyResponseCode.LOGIN_AUTH_ERROR.getResponseMessage());
      }

      Session session = subject.getSession();
      session.setAttribute("loginUser", subject.getPrincipal());
      String userName = Objects.toString(subject.getPrincipal());
      String channelId = ctx.channel().id().asLongText();
      this.logger.info("[{}]用户登录成功，缓存Channel及Session信息，id分别为：[{}]，[{}]", new Object[]{userName, channelId, session.getId()});
      return "{'responseCode':'200','responseMessage':'操作成功'}";
   }
}
