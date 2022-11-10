package com.aqmd.netty.annotation;

import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

public class HawkMethodHandler {
   private final Logger logger = LoggerFactory.getLogger(HawkMethodHandler.class);
   private Object handler;
   private Method handlerMethod;
   private HawkMethodValue hawkMethodValue;

   public Object getHandler() {
      return this.handler;
   }

   public void setHandler(Object handler) {
      this.handler = handler;
   }

   public Method getHandlerMethod() {
      return this.handlerMethod;
   }

   public void setHandlerMethod(Method handlerMethod) {
      this.handlerMethod = handlerMethod;
   }

   public HawkMethodValue getHawkMethodValue() {
      return this.hawkMethodValue;
   }

   public void setHawkMethodValue(HawkMethodValue hawkMethodValue) {
      this.hawkMethodValue = hawkMethodValue;
   }

   public Object doInvoke(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx) {
      Class<?>[] paramterTypes = this.getHandlerMethod().getParameterTypes();
      List<Object> params = new ArrayList();
      if (paramterTypes.length > 0) {
         Class[] var6 = paramterTypes;
         int var7 = paramterTypes.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Class<?> paramterType = var6[var8];
            if (ClassUtils.isAssignable(byte[].class, paramterType)) {
               params.add(request.getBody());
            } else if (ClassUtils.isAssignable(ChannelHandlerContext.class, paramterType)) {
               params.add(ctx);
            } else if (ClassUtils.isAssignable(Long.TYPE, paramterType)) {
               params.add(request.getSequenceId());
            }
         }
      }

      try {
         Object result = this.getHandlerMethod().invoke(this.getHandler(), params.toArray());
         if (result instanceof String) {
            response.setBody(((String)result).getBytes());
         } else {
            MessageLite message = (MessageLite)result;
            response.setBody(message.toByteArray());
         }

         response.setCode(NettyResponseCode.SUCCESS.getResponseCode());
         return result;
      } catch (IllegalAccessException var10) {
         this.logger.error(var10.getMessage(), var10);
         throw new NettyException(var10, NettyResponseCode.HANDLER_ACCESS_ERROR.getResponseCode() + "~" + NettyResponseCode.HANDLER_ACCESS_ERROR.getResponseMessage());
      } catch (IllegalArgumentException var11) {
         this.logger.error(var11.getMessage(), var11);
         throw new NettyException(var11, NettyResponseCode.HANDLER_ARGUMENT_ERROR.getResponseCode() + "~" + NettyResponseCode.HANDLER_ARGUMENT_ERROR.getResponseMessage());
      } catch (InvocationTargetException var12) {
         Throwable cause = var12.getCause();
         if (cause instanceof NettyException) {
            throw new NettyException(cause.getMessage());
         } else {
            this.logger.error(NettyResponseCode.HANDLER_INVOCATE_ERROR.getResponseMessage(), var12);
            throw new NettyException(NettyResponseCode.HANDLER_INVOCATE_ERROR.getResponseString());
         }
      }
   }
}
