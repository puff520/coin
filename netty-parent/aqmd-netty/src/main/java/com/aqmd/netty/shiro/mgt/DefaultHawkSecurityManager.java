package com.aqmd.netty.shiro.mgt;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.session.DefaultHawkSessionContext;
import com.aqmd.netty.shiro.subject.DefaultHawkSubjectContext;
import com.aqmd.netty.shiro.subject.HawkSubjectContext;
import com.aqmd.netty.shiro.util.HawkUtils;
import java.io.Serializable;
import java.util.Map;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.subject.SubjectContext;

public class DefaultHawkSecurityManager extends DefaultSecurityManager implements SecurityManager {
   protected SubjectContext copy(SubjectContext subjectContext) {
      return (SubjectContext)(subjectContext instanceof HawkSubjectContext ? new DefaultHawkSubjectContext((HawkSubjectContext)subjectContext) : super.copy(subjectContext));
   }

   protected SessionContext createSessionContext(SubjectContext subjectContext) {
      SessionContext sessionContext = super.createSessionContext(subjectContext);
      if (subjectContext instanceof HawkSubjectContext) {
         HawkSubjectContext wsc = (HawkSubjectContext)subjectContext;
         RequestPacket request = wsc.resolveHawkRequest();
         ResponsePacket response = wsc.resolveHawkResponse();
         DefaultHawkSessionContext webSessionContext = new DefaultHawkSessionContext((Map)sessionContext);
         if (request != null) {
            webSessionContext.setHawkRequest(request);
         }

         if (response != null) {
            webSessionContext.setHawkResponse(response);
         }

         sessionContext = webSessionContext;
      }

      return (SessionContext)sessionContext;
   }

   protected SessionKey getSessionKey(SubjectContext context) {
      Serializable sessionId = context.getSessionId();
      RequestPacket request = HawkUtils.getRequest(context);
      ResponsePacket response = HawkUtils.getResponse(context);
      return new HawkSessionKey(sessionId, request, response);
   }
}
