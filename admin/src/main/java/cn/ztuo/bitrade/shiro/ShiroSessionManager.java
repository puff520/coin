package cn.ztuo.bitrade.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionKey;

import javax.servlet.ServletRequest;
import java.io.Serializable;

public class ShiroSessionManager extends DefaultWebSessionManager {

    /**
     * 防止每次请求对redis高达十几次的访问
     * 所以将用户sessionId存储到request请求中
     * 登录成功后每一次访问都从request中取
     *
     */
    @Override
    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        ServletRequest request = null;
        if (sessionKey instanceof WebSessionKey) {
            request = ((WebSessionKey) sessionKey).getServletRequest();
        }
        Serializable sessionId = getSessionId(sessionKey);
        if (null != request && null != sessionId) {
            Object sessionObj = request.getAttribute(sessionId.toString());
            if (sessionObj != null) {
                return (Session) sessionObj;
            }
        }
        Session session = null;
        try {
            session = super.retrieveSession(sessionKey);
        } catch (UnknownSessionException e) {
            return null;
        }
        if (null != request && null != sessionId) {
            request.setAttribute(sessionId.toString(), session);
        }
        return session;
    }
}
