package com.aqmd.netty.filter;

import com.aqmd.netty.annotation.HawkFilter;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import io.netty.channel.ChannelHandlerContext;
import java.io.IOException;

public abstract class HFilter {
   public abstract void init() throws NettyException;

   public abstract void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx, FilterChain chain) throws IOException, NettyException;

   public abstract void destroy();

   protected String buildExceptionMsg(int code, String message) {
      return code + "~" + message;
   }

   public boolean isMatch(RequestPacket req) {
      HawkFilter hawkFilter = (HawkFilter)this.getClass().getAnnotation(HawkFilter.class);
      int[] var3 = hawkFilter.ignoreCmds();
      int var4 = var3.length;

      int var5;
      int cmd;
      for(var5 = 0; var5 < var4; ++var5) {
         cmd = var3[var5];
         if (cmd == req.getCmd()) {
            return false;
         }
      }

      var3 = hawkFilter.cmds();
      var4 = var3.length;

      for(var5 = 0; var5 < var4; ++var5) {
         cmd = var3[var5];
         if (cmd == req.getCmd()) {
            return true;
         }
      }

      return true;
   }
}
