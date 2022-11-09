package com.aqmd.netty.shiro.mgt;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.subject.HawkSubjectContext;
import com.aqmd.netty.shiro.subject.support.HawkDelegatingSubject;
import org.apache.shiro.mgt.DefaultSubjectFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;

public class DefaultHawkSubjectFactory extends DefaultSubjectFactory {
   public Subject createSubject(SubjectContext context) {
      if (!(context instanceof HawkSubjectContext)) {
         return super.createSubject(context);
      } else {
         HawkSubjectContext wsc = (HawkSubjectContext)context;
         SecurityManager securityManager = wsc.resolveSecurityManager();
         Session session = wsc.resolveSession();
         boolean sessionEnabled = wsc.isSessionCreationEnabled();
         PrincipalCollection principals = wsc.resolvePrincipals();
         boolean authenticated = wsc.resolveAuthenticated();
         String host = wsc.resolveHost();
         RequestPacket request = wsc.resolveHawkRequest();
         ResponsePacket response = wsc.resolveHawkResponse();
         return new HawkDelegatingSubject(principals, authenticated, host, session, sessionEnabled, request, response, securityManager);
      }
   }

   /** @deprecated */
   @Deprecated
   protected Subject newSubjectInstance(PrincipalCollection principals, boolean authenticated, String host, Session session, RequestPacket request, ResponsePacket response, SecurityManager securityManager) {
      return new HawkDelegatingSubject(principals, authenticated, host, session, true, request, response, securityManager);
   }
}
