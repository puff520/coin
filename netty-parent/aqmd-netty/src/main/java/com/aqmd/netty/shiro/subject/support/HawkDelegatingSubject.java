package com.aqmd.netty.shiro.subject.support;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.mgt.HawkSessionContext;
import com.aqmd.netty.shiro.session.DefaultHawkSessionContext;
import com.aqmd.netty.shiro.subject.HawkSubject;
import com.aqmd.netty.shiro.util.HawkUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DelegatingSubject;
import org.apache.shiro.util.StringUtils;

public class HawkDelegatingSubject extends DelegatingSubject implements HawkSubject {
   private static final long serialVersionUID = -1655724323350159250L;
   private final RequestPacket requestPacket;
   private final ResponsePacket responsePacket;

   public HawkDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, RequestPacket request, ResponsePacket response, SecurityManager securityManager) {
      this(principals, authenticated, host, session, true, request, response, securityManager);
   }

   public HawkDelegatingSubject(PrincipalCollection principals, boolean authenticated, String host, Session session, boolean sessionEnabled, RequestPacket request, ResponsePacket response, SecurityManager securityManager) {
      super(principals, authenticated, host, session, sessionEnabled, securityManager);
      this.requestPacket = request;
      this.responsePacket = response;
   }

   public RequestPacket getHawkRequest() {
      return this.requestPacket;
   }

   public ResponsePacket getHawkResponse() {
      return this.responsePacket;
   }

   protected boolean isSessionCreationEnabled() {
      boolean enabled = super.isSessionCreationEnabled();
      return enabled && HawkUtils._isSessionCreationEnabled(this);
   }

   protected SessionContext createSessionContext() {
      HawkSessionContext hsc = new DefaultHawkSessionContext();
      String host = this.getHost();
      if (StringUtils.hasText(host)) {
         hsc.setHost(host);
      }

      hsc.setHawkRequest(this.requestPacket);
      hsc.setHawkResponse(this.responsePacket);
      return hsc;
   }
}
