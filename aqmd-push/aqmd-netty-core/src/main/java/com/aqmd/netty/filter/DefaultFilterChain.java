package com.aqmd.netty.filter;

import com.aqmd.netty.annotation.HawkFilterValue;
import com.aqmd.netty.annotation.HawkMethodHandler;
import com.aqmd.netty.common.constant.NettyResponseCode;
import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.exception.NettyException;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class DefaultFilterChain implements FilterChain {
   private List<HawkFilterValue> filters = new ArrayList();
   private HawkMethodHandler handler;
   private int _iter = 0;
   private boolean handlerExecFlag = false;

   public DefaultFilterChain(TreeSet<HawkFilterValue> treeFilters, HawkMethodHandler handler) {
      this.handler = handler;
      Iterator var3 = treeFilters.iterator();

      while(var3.hasNext()) {
         HawkFilterValue filterValue = (HawkFilterValue)var3.next();
         this.filters.add(filterValue);
      }

   }

   public void doFilter(RequestPacket request, ResponsePacket response, ChannelHandlerContext ctx) {
      if (CollectionUtils.isEmpty(this.filters)) {
         if (this.handler == null) {
            throw new NettyException(NettyResponseCode.NO_HANDLER_ERROR.getResponseCode() + "~" + NettyResponseCode.NO_HANDLER_ERROR.getResponseMessage());
         } else {
            this.handler.doInvoke(request, response, ctx);
            this.handlerExecFlag = true;
         }
      } else {
         try {
            while(this._iter < this.filters.size()) {
               HawkFilterValue filterValue;
               if ((filterValue = (HawkFilterValue)this.filters.get(this._iter)).getHfilter().isMatch(request)) {
                  ++this._iter;
                  HFilter hFilter = filterValue.getHfilter();
                  hFilter.doFilter(request, response, ctx, this);
                  break;
               }

               ++this._iter;
            }

            if (this._iter == this.filters.size() && !this.handlerExecFlag) {
               this.handler.doInvoke(request, response, ctx);
               this.handlerExecFlag = true;
            }

         } catch (IOException var6) {
            throw new NettyException(NettyResponseCode.FILTER_IO_ERROR.getResponseCode() + "~" + var6.getMessage());
         } catch (RuntimeException var7) {
            throw var7;
         } catch (Exception var8) {
            throw new NettyException(NettyResponseCode.UNKNOW_ERROR.getResponseCode() + "~" + NettyResponseCode.UNKNOW_ERROR.getResponseMessage());
         }
      }
   }
}
