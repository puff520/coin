package com.aqmd.netty.common.constant;

public class NettyResponseCode {
   public static final NettyResponseBean SUCCESS = new NettyResponseBean(200, "操作成功");
   public static final NettyResponseBean ASYNC_SUCCESS = new NettyResponseBean(201, "异步请求操作成功");
   public static final NettyResponseBean REQUEST_ERROR = new NettyResponseBean(502, "请求执行错误");
   public static final NettyResponseBean UNKNOW_ERROR = new NettyResponseBean(500, "未知错误");
   public static final NettyResponseBean BODY_FORMAT_ERROR = new NettyResponseBean(501, "protobuffer内容体格式化错误");
   public static final NettyResponseBean CMD_NOT_FOUND = new NettyResponseBean(404, "指令不存在");
   public static final NettyResponseBean OBSOLETED_METHOD = new NettyResponseBean(405, "过期方法");
   public static final NettyResponseBean UTF8_ENCODING_ERROR = new NettyResponseBean(503, "内容无法进行UTF-8转换");
   public static final NettyResponseBean NOLOGIN_ERROR = new NettyResponseBean(504, "用户会话失效，请重新登录！");
   public static final NettyResponseBean LOGIN_AUTH_ERROR = new NettyResponseBean(505, "登录失败，用户名或密码错误！");
   public static final NettyResponseBean NO_HANDLER_ERROR = new NettyResponseBean(506, "未找到请求对应的处理器");
   public static final NettyResponseBean FILTER_IO_ERROR = new NettyResponseBean(507, "过滤器IO异常");
   public static final NettyResponseBean HANDLER_ACCESS_ERROR = new NettyResponseBean(508, "Handler访问权限错误");
   public static final NettyResponseBean HANDLER_ARGUMENT_ERROR = new NettyResponseBean(509, "Handler访问参数错误");
   public static final NettyResponseBean HANDLER_INVOCATE_ERROR = new NettyResponseBean(510, "Handler调用异常错误");
}
