package com.aqmd.netty.shiro.session;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.mgt.HawkSessionContext;
import java.util.Map;
import org.apache.shiro.session.mgt.DefaultSessionContext;

public class DefaultHawkSessionContext extends DefaultSessionContext implements HawkSessionContext {
   private static final long serialVersionUID = -3974604687792523072L;
   private static final String HAWK_REQUEST = DefaultHawkSessionContext.class.getName() + ".HAWK_REQUEST";
   private static final String HAWK_RESPONSE = DefaultHawkSessionContext.class.getName() + ".HAWK_RESPONSE";

   public DefaultHawkSessionContext() {
   }

   public DefaultHawkSessionContext(Map<String, Object> map) {
      super(map);
   }

   public void setHawkRequest(RequestPacket request) {
      if (request != null) {
         this.put(HAWK_REQUEST, request);
      }

   }

   public RequestPacket getHawkRequest() {
      return (RequestPacket)this.getTypedValue(HAWK_REQUEST, RequestPacket.class);
   }

   public void setHawkResponse(ResponsePacket response) {
      if (response != null) {
         this.put(HAWK_RESPONSE, response);
      }

   }

   public ResponsePacket getHawkResponse() {
      return (ResponsePacket)this.getTypedValue(HAWK_RESPONSE, ResponsePacket.class);
   }
}
