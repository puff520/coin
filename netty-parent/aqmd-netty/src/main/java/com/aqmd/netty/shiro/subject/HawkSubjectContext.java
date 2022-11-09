package com.aqmd.netty.shiro.subject;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.util.RequestPairSource;
import org.apache.shiro.subject.SubjectContext;

public interface HawkSubjectContext extends SubjectContext, RequestPairSource {
   RequestPacket getHawkRequest();

   void setHawkRequest(RequestPacket request);

   RequestPacket resolveHawkRequest();

   ResponsePacket getHawkResponse();

   void setHawkResponse(ResponsePacket response);

   ResponsePacket resolveHawkResponse();
}
