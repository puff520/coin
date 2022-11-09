package com.aqmd.netty.context;

import com.aqmd.netty.annotation.HawkBean;
import com.aqmd.netty.annotation.HawkFilter;
import com.aqmd.netty.annotation.HawkFilterValue;
import com.aqmd.netty.annotation.HawkMethod;
import com.aqmd.netty.annotation.HawkMethodHandler;
import com.aqmd.netty.annotation.HawkMethodValue;
import com.aqmd.netty.annotation.ObsoletedType;
import com.aqmd.netty.exception.NettyException;
import com.aqmd.netty.filter.HFilter;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

public class HawkContext implements BeanPostProcessor {
   protected final Logger logger = LoggerFactory.getLogger(this.getClass());
   private Map<String, HawkMethodHandler> hawkMethodHandlerMap = new HashMap();
   private TreeSet<HawkFilterValue> filters = new TreeSet();

   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
      if (AnnotationUtils.findAnnotation(bean.getClass(), HawkBean.class) != null) {
         ReflectionUtils.doWithMethods(bean.getClass(), (method) -> {
            HawkMethod hawkMethod = (HawkMethod)AnnotationUtils.findAnnotation(method, HawkMethod.class);
            HawkMethodValue HawkMethodValue = new HawkMethodValue(hawkMethod.cmd(), hawkMethod.version(), ObsoletedType.isObsoleted(hawkMethod.obsoleted()));
            HawkMethodHandler HawkMethodHandler = new HawkMethodHandler();
            HawkMethodHandler.setHawkMethodValue(HawkMethodValue);
            HawkMethodHandler.setHandler(bean);
            HawkMethodHandler.setHandlerMethod(method);
            String handlerKey = this.buildeHandlerKey(HawkMethodValue.getCmd(), HawkMethodValue.getVersion());
            if (this.hawkMethodHandlerMap.get(handlerKey) != null) {
               throw new NettyException("重复的指令， " + handlerKey);
            } else if (method.getParameterTypes().length > 3) {
               throw new NettyException(String.format("%s#%s最多包含三个个参数", method.getDeclaringClass().getCanonicalName(), method.getName()));
            } else {
               if (method.getParameterTypes().length == 1) {
                  if (!ClassUtils.isAssignable(Long.TYPE, method.getParameterTypes()[0]) && !ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[0]) && !ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[0])) {
                     throw new NettyException(String.format("允许%s#%s(long)、(byte[])或(ChannelHandlerContext）", method.getDeclaringClass().getCanonicalName(), method.getName()));
                  }
               } else if (method.getParameterTypes().length == 2) {
                  boolean fail = true;
                  if (ClassUtils.isAssignable(Long.TYPE, method.getParameterTypes()[0]) && (ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[1]) || ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[1]))) {
                     fail = false;
                  }

                  if (ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[0]) && (ClassUtils.isAssignable(Long.TYPE, method.getParameterTypes()[1]) || ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[1]))) {
                     fail = false;
                  }

                  if (ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[0]) && (ClassUtils.isAssignable(Long.TYPE, method.getParameterTypes()[1]) || ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[1]))) {
                     fail = false;
                  }

                  if (fail) {
                     throw new NettyException(String.format("允许%s#%s(long, byte[])或(long, ChannelHandlerContext)、(byte[], long)或(byte[], ChannelHandlerContext)、(ChannelHandlerContext, long)或(ChannelHandlerContext, byte[])", method.getDeclaringClass().getCanonicalName(), method.getName()));
                  }
               } else if (method.getParameterTypes().length == 3 && (!ClassUtils.isAssignable(Long.TYPE, method.getParameterTypes()[0]) || !ClassUtils.isAssignable(byte[].class, method.getParameterTypes()[1]) || !ClassUtils.isAssignable(ChannelHandlerContext.class, method.getParameterTypes()[2]))) {
                  throw new NettyException(String.format("允许%s#%s(long, byte[], ChannelHandlerContext)", method.getDeclaringClass().getCanonicalName(), method.getName()));
               }

               this.hawkMethodHandlerMap.put(handlerKey, HawkMethodHandler);
               this.logger.info(String.format("注册指令%s", handlerKey));
            }
         }, (method) -> {
            return !method.isSynthetic() && AnnotationUtils.findAnnotation(method, HawkMethod.class) != null;
         });
      }

      HawkFilter HawkFilter = (HawkFilter)AnnotationUtils.findAnnotation(bean.getClass(), HawkFilter.class);
      if (HawkFilter != null) {
         this.logger.info(String.format("增加过滤器%s", bean.getClass()));
         this.filters.add(new HawkFilterValue(HawkFilter.order(), HawkFilter.cmds(), HawkFilter.ignoreCmds(), (HFilter)bean));
      }

      return bean;
   }

   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      return bean;
   }

   public HawkMethodHandler getHawkMethodHandler(int cmd, int version) {
      String handlerKey = this.buildeHandlerKey(cmd, version);
      HawkMethodHandler handler = (HawkMethodHandler)this.hawkMethodHandlerMap.get(handlerKey);
      return handler;
   }

   public TreeSet<HawkFilterValue> getFilters() {
      return this.filters;
   }

   private String buildeHandlerKey(int cmd, int version) {
      return cmd + "#" + version;
   }
}
