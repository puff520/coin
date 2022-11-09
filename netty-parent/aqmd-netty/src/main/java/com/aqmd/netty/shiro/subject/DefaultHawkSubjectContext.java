package com.aqmd.netty.shiro.subject;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;

public class DefaultHawkSubjectContext extends DefaultSubjectContext implements HawkSubjectContext {
   private static final long serialVersionUID = 8188555355305827739L;
   private static final String HAWK_REQUEST = DefaultHawkSubjectContext.class.getName() + ".HAWK_REQUEST";
   private static final String HAWK_RESPONSE = DefaultHawkSubjectContext.class.getName() + ".HAWK_RESPONSE";

   public DefaultHawkSubjectContext() {
   }

   public DefaultHawkSubjectContext(HawkSubjectContext context) {
      super(context);
   }

   public RequestPacket getHawkRequest() {
      return (RequestPacket)this.getTypedValue(HAWK_REQUEST, RequestPacket.class);
   }

   public void setHawkRequest(RequestPacket request) {
      if (request != null) {
         this.put(HAWK_REQUEST, request);
      }

   }

   public RequestPacket resolveHawkRequest() {
      RequestPacket request = this.getHawkRequest();
      if (request == null) {
         Subject existing = this.getSubject();
         if (existing instanceof HawkSubject) {
            request = ((HawkSubject)existing).getHawkRequest();
         }
      }

      return request;
   }

   public ResponsePacket getHawkResponse() {
      return (ResponsePacket)this.getTypedValue(HAWK_RESPONSE, ResponsePacket.class);
   }

   public void setHawkResponse(ResponsePacket response) {
      if (response != null) {
         this.put(HAWK_RESPONSE, response);
      }

   }

   public ResponsePacket resolveHawkResponse() {
      ResponsePacket response = this.getHawkResponse();
      if (response == null) {
         Subject existing = this.getSubject();
         if (existing instanceof HawkSubject) {
            response = ((HawkSubject)existing).getHawkResponse();
         }
      }

      return response;
   }
}
