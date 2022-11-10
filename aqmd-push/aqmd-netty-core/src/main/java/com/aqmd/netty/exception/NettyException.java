package com.aqmd.netty.exception;

import com.aqmd.netty.common.constant.NettyResponseBean;

public class NettyException extends BaseException {
   private static final long serialVersionUID = -3711290613973933714L;

   public NettyException(String code) {
      super(code, (Throwable)null, code, (Object[])null);
   }

   public NettyException(NettyResponseBean responseBean) {
      super(responseBean.getResponseString(), (Throwable)null, responseBean.getResponseString(), (Object[])null);
   }

   public NettyException(Throwable cause, String code) {
      super(code, cause, code, (Object[])null);
   }

   public NettyException(String code, Object[] values) {
      super(code, (Throwable)null, code, values);
   }

   public NettyException(Throwable cause, String code, Object[] values) {
      super(code, (Throwable)null, code, values);
   }
}
