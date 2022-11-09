package com.aqmd.netty.shiro.subject;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

public interface HawkSubject extends Subject {
   RequestPacket getHawkRequest();

   ResponsePacket getHawkResponse();

   public static class Builder extends Subject.Builder {
      public Builder(RequestPacket request, ResponsePacket response) {
         this(SecurityUtils.getSecurityManager(), request, response);
      }

      public Builder(SecurityManager securityManager, RequestPacket request, ResponsePacket response) {
         super(securityManager);
         if (request == null) {
            throw new IllegalArgumentException("HawkRequest argument cannot be null.");
         } else if (response == null) {
            throw new IllegalArgumentException("HawkResponse argument cannot be null.");
         } else {
            this.setRequest(request);
            this.setResponse(response);
         }
      }

      protected SubjectContext newSubjectContextInstance() {
         return new DefaultHawkSubjectContext();
      }

      protected Builder setRequest(RequestPacket request) {
         if (request != null) {
            ((HawkSubjectContext)this.getSubjectContext()).setHawkRequest(request);
         }

         return this;
      }

      protected Builder setResponse(ResponsePacket response) {
         if (response != null) {
            ((HawkSubjectContext)this.getSubjectContext()).setHawkResponse(response);
         }

         return this;
      }

      public HawkSubject buildHawkSubject() {
         Subject subject = super.buildSubject();
         if (!(subject instanceof HawkSubject)) {
            String msg = "Subject implementation returned from the SecurityManager was not a " + HawkSubject.class.getName() + " implementation.  Please ensure a Hawk-enabled SecurityManager has been configured and made available to this builder.";
            throw new IllegalStateException(msg);
         } else {
            return (HawkSubject)subject;
         }
      }
   }
}
