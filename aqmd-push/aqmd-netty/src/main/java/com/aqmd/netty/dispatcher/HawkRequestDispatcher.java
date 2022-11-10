package com.aqmd.netty.dispatcher;

import com.aqmd.netty.Dispatcher;
import com.aqmd.netty.annotation.HawkMethodHandler;
import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.context.HawkContext;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.entity.HawkResponseMessage.CommonResult;
import com.aqmd.netty.exception.NettyException;
import com.aqmd.netty.filter.DefaultFilterChain;
import com.aqmd.netty.filter.FilterChain;
import com.google.common.base.Throwables;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class HawkRequestDispatcher implements Dispatcher<RequestPacket, ResponsePacket> {
   private final Logger logger = LoggerFactory.getLogger(HawkRequestDispatcher.class);
   @Autowired
   private HawkContext hawkContext;

   public ResponsePacket dispatch(RequestPacket request, ChannelHandlerContext ctx) throws NettyException {
      HawkMethodHandler HawkMethodHandler = this.hawkContext.getHawkMethodHandler(request.getCmd(), request.getVersion());
      ResponsePacket response = new ResponsePacket();
      response.setSequenceId(request.getSequenceId());
      response.setRequestId(request.getRequestId());
      response.setCmd(request.getCmd());
      if (HawkMethodHandler == null) {
         this.logger.error("指令{}#{}不存在", request.getCmd(), request.getVersion());
         response.setCode(NettyResponseCode.CMD_NOT_FOUND.getResponseCode());
         response.setBody(CommonResult.newBuilder().setResultCode(NettyResponseCode.CMD_NOT_FOUND.getResponseCode()).setResultMsg(NettyResponseCode.CMD_NOT_FOUND.getResponseMessage()).build().toByteArray());
         return response;
      } else if (HawkMethodHandler.getHawkMethodValue().isObsoleted()) {
         this.logger.error("指令{}#{}已过期", request.getCmd(), request.getVersion());
         response.setCode(NettyResponseCode.OBSOLETED_METHOD.getResponseCode());
         response.setBody(CommonResult.newBuilder().setResultCode(NettyResponseCode.OBSOLETED_METHOD.getResponseCode()).setResultMsg(NettyResponseCode.OBSOLETED_METHOD.getResponseMessage()).build().toByteArray());
         return response;
      } else {
         return this.doInvoke(request, ctx, HawkMethodHandler, response);
      }
   }

   private ResponsePacket doInvoke(RequestPacket request, ChannelHandlerContext ctx, HawkMethodHandler hawkMethodHandler, ResponsePacket response) {
      try {
         FilterChain chain = new DefaultFilterChain(this.hawkContext.getFilters(), hawkMethodHandler);
         chain.doFilter(request, response, ctx);
      } catch (RuntimeException var6) {
         var6.printStackTrace();
         this.logger.error("指令{}#{}业务异常，message={}", new Object[]{request.getCmd(), request.getVersion(), var6.getMessage()});
         response.setCode(NettyResponseCode.REQUEST_ERROR.getResponseCode());
         this.buildExceptionBody(response, var6.getMessage());
      } catch (Exception var7) {
         response.setCode(NettyResponseCode.REQUEST_ERROR.getResponseCode());
         if (!(var7 instanceof InvalidProtocolBufferException) && !(var7.getCause() instanceof InvalidProtocolBufferException)) {
            this.logger.error("指令{}#{}未知错误, {}", new Object[]{request.getCmd(), request.getVersion(), Throwables.getStackTraceAsString(var7), var7});
            response.setBody(CommonResult.newBuilder().setResultCode(NettyResponseCode.UNKNOW_ERROR.getResponseCode()).setResultMsg(var7.getMessage()).build().toByteArray());
         } else {
            this.logger.error("指令{}#{}数据包格式错误，{}", new Object[]{request.getCmd(), request.getVersion(), Throwables.getStackTraceAsString(var7)});
            response.setBody(CommonResult.newBuilder().setResultCode(NettyResponseCode.BODY_FORMAT_ERROR.getResponseCode()).setResultMsg(var7.getMessage()).build().toByteArray());
         }
      }

      return response;
   }

   private void buildExceptionBody(ResponsePacket response, String message) {
      String[] results = message.split("~");
      response.setBody(CommonResult.newBuilder().setResultCode(Integer.parseInt(results[0])).setResultMsg(results[1]).build().toByteArray());
   }
}
