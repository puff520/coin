package com.aqmd.netty.shiro.mgt;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.util.RequestPairSource;
import org.apache.shiro.session.mgt.SessionContext;

public interface HawkSessionContext extends SessionContext, RequestPairSource {
   RequestPacket getHawkRequest();

   void setHawkRequest(RequestPacket request);

   ResponsePacket getHawkResponse();

   void setHawkResponse(ResponsePacket response);
}
