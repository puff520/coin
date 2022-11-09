package com.aqmd.netty.shiro.session;

import com.aqmd.netty.entity.RequestPacket;
import com.aqmd.netty.entity.ResponsePacket;
import com.aqmd.netty.shiro.listener.HSessionListener;
import com.aqmd.netty.shiro.mgt.HawkSessionKey;
import com.aqmd.netty.shiro.util.HawkUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.DelegatingSession;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SessionManager;

public class DefaultHawkSessionManager extends DefaultSessionManager implements SessionManager {
   public DefaultHawkSessionManager() {
      List<SessionListener> listeners = new ArrayList();
      listeners.add(new HSessionListener());
      super.setSessionListeners(listeners);
   }

   private Serializable getReferencedSessionId(RequestPacket request, ResponsePacket response) {
      return request.getSequenceId();
   }

   public Serializable getSessionId(SessionKey key) {
      super.getSessionId(key);
      RequestPacket request = HawkUtils.getRequest(key);
      ResponsePacket response = HawkUtils.getResponse(key);
      Serializable id = this.getSessionId(request, response);
      return id;
   }

   protected Serializable getSessionId(RequestPacket request, ResponsePacket response) {
      return this.getReferencedSessionId(request, response);
   }

   protected Session createExposedSession(Session session, SessionContext context) {
      RequestPacket request = HawkUtils.getRequest(context);
      ResponsePacket response = HawkUtils.getResponse(context);
      SessionKey key = new HawkSessionKey(session.getId(), request, response);
      return new DelegatingSession(this, key);
   }

   protected Session createExposedSession(Session session, SessionKey key) {
      RequestPacket request = HawkUtils.getRequest(key);
      ResponsePacket response = HawkUtils.getResponse(key);
      SessionKey sessionKey = new HawkSessionKey(session.getId(), request, response);
      return new DelegatingSession(this, sessionKey);
   }
}
