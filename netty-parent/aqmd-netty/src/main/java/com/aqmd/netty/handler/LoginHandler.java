package com.aqmd.netty.handler;

import com.aqmd.netty.annotation.HawkBean;
import com.aqmd.netty.annotation.HawkMethod;
import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.entity.HawkResponseMessage;
import com.aqmd.netty.entity.LoginMessage;
import com.aqmd.netty.entity.HawkResponseMessage.CommonResult;
import com.aqmd.netty.entity.LoginMessage.LoginUser;
import com.aqmd.netty.exception.NettyException;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandlerContext;
import java.net.InetSocketAddress;
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
public class LoginHandler {
   protected final Logger logger = LoggerFactory.getLogger(this.getClass());

   @HawkMethod(
      cmd = 11002,
      version = 1
   )
   public HawkResponseMessage.CommonResult login(long seqId, byte[] body, ChannelHandlerContext ctx) {
      Subject subject = SecurityUtils.getSubject();
      String loginIp = "";

      try {
         LoginMessage.LoginUser user = ((LoginMessage.LoginUser.Builder)LoginUser.newBuilder().mergeFrom(body)).build();
         UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPasswd());
         subject.login(token);
         loginIp = ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress().getHostAddress();
      } catch (InvalidProtocolBufferException var11) {
         this.logger.error(NettyResponseCode.BODY_FORMAT_ERROR.getResponseString());
         throw new NettyException(var11, NettyResponseCode.BODY_FORMAT_ERROR.getResponseString());
      } catch (IncorrectCredentialsException | UnknownAccountException var12) {
         throw new NettyException(var12, NettyResponseCode.LOGIN_AUTH_ERROR.getResponseCode() + "~" + NettyResponseCode.LOGIN_AUTH_ERROR.getResponseMessage());
      }

      Session session = subject.getSession();
      session.setAttribute("loginUser", subject.getPrincipal());
      String userName = Objects.toString(subject.getPrincipal());
      String channelId = ctx.channel().id().asLongText();
      this.logger.info("[{}]用户登录成功,登陆ip为[{}]，缓存Channel及Session信息，id分别为：[{}]，[{}]", new Object[]{userName, loginIp, channelId, session.getId()});
      return CommonResult.newBuilder().setResultCode(NettyResponseCode.SUCCESS.getResponseCode()).setResultMsg(NettyResponseCode.SUCCESS.getResponseMessage()).build();
   }
}
