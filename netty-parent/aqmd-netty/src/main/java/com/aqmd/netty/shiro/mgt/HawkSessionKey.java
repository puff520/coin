package com.aqmd.netty.shiro.mgt;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.util.RequestPairSource;
import java.io.Serializable;
import org.apache.shiro.session.mgt.DefaultSessionKey;

public class HawkSessionKey extends DefaultSessionKey implements RequestPairSource {
   private final RequestPacket requestPacket;
   private final ResponsePacket responsePacket;

   public HawkSessionKey(RequestPacket request, ResponsePacket response) {
      if (request == null) {
         throw new NullPointerException("request argument cannot be null.");
      } else if (response == null) {
         throw new NullPointerException("response argument cannot be null.");
      } else {
         this.requestPacket = request;
         this.responsePacket = response;
      }
   }

   public HawkSessionKey(Serializable sessionId, RequestPacket request, ResponsePacket response) {
      this(request, response);
      this.setSessionId(sessionId);
   }

   public RequestPacket getHawkRequest() {
      return this.requestPacket;
   }

   public ResponsePacket getHawkResponse() {
      return this.responsePacket;
   }
}
