package cn.ztuo.bitrade.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * redisSessionDAO shiro sessionDAO层的实现 通过redis
 * 使用的是shiro-redis开源插件
 *
 * @author oyj
 * @date 2019-06-23 22:36
 */
@Component
public class RedisSessionDAO extends AbstractSessionDAO {

    @Resource
    private RedisTemplate redisTemplate;

    // Session失效时间，单位为毫秒
    private long expireTime = 1000 * 60 * 30;

    private String prefix = "SHIRO:SESSIONID:";

    public RedisSessionDAO() {
        super();
    }

    public RedisSessionDAO(long expireTime, RedisTemplate redisTemplate) {
        super();
        this.expireTime = expireTime;
        this.redisTemplate = redisTemplate;
    }

    // 更新session
    @Override
    public void update(Session session) throws UnknownSessionException {
        if (session == null || session.getId() == null) {
            return;
        }

        //app登录永不过期
        long timeout = expireTime;
        if ("app".equals(session.getAttribute("loginType"))) {
            timeout = -1000;
            redisTemplate.opsForValue().set(prefix + session.getId(), session);
        } else {
            redisTemplate.opsForValue().set(prefix + session.getId(), session, timeout, TimeUnit.MILLISECONDS);
        }
        session.setTimeout(timeout);
    }

    // 删除session
    @Override
    public void delete(Session session) {
        if (null == session) {
            return;
        }
        redisTemplate.opsForValue().getOperations().delete(prefix + session.getId());
    }

    // 获取活跃的session，可以用来统计在线人数，如果要实现这个功能
    // 可以在将session加入redis时指定一个session前缀
    // 统计的时候则使用keys("prefix + *")的方式来模糊查找redis中所有的session集合
    @Override
    public Collection<Session> getActiveSessions() {
        return redisTemplate.keys(prefix + "*");
    }

    // 加入session
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);

        return sessionId;
    }

    // 读取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        if (sessionId == null) {
            return null;
        }

        Session session = (Session) redisTemplate.opsForValue().get(prefix + sessionId);
        return session;
    }



}
