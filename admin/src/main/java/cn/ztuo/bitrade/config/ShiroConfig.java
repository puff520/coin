package cn.ztuo.bitrade.config;

import cn.ztuo.bitrade.core.AdminRealm;
import cn.ztuo.bitrade.shiro.RedisSessionDAO;
import cn.ztuo.bitrade.shiro.ShiroSessionManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author GS
 * @date 2017年12月19日
 */
@Slf4j
@Configuration
public class ShiroConfig {

    @Resource
    private RedisSessionDAO redisSessionDAO;

    /**
     * 自定义sessionManager
     *
     * @param simpleCookie
     * @return
     */
    @Bean("sessionManager")
    public SessionManager sessionManager(@Qualifier("sessionIdCookie") SimpleCookie simpleCookie) {
        ShiroSessionManager sessionManager = new ShiroSessionManager();
        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为12h
        sessionManager.setGlobalSessionTimeout(1000 * 60 * 60 * 12);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //配置监听session，缓存包含用户信息的session到redis中
        sessionManager.setSessionDAO(redisSessionDAO);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler
        //底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        sessionManager.setSessionValidationInterval(1000 * 60 * 30);
        //配置保存sessionId的cookie
        sessionManager.setSessionIdCookie(simpleCookie);
        return sessionManager;
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     *
     * @param securityManager
     * @return
     */

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        log.info("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/captcha", "anon");
        filterChainDefinitionMap.put("/admin/code/**", "anon");
        filterChainDefinitionMap.put("admin/**/page-query", "user");
        filterChainDefinitionMap.put("/admin/employee/logout", "logout");
        filterChainDefinitionMap.put("admin/**/detail", "authc");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        /*shiroFilterFactoryBean.setU("/403");*/
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 设置rememberMe  Cookie 7天
     *
     * @return
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie getSessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("SHIROSESSIONID");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setMaxAge(7 * 24 * 60 * 60);
        return simpleCookie;
    }


    /**
     * cookie 管理器
     *
     * @return
     */
    @Bean(name = "cookieRememberMeManager")
    @DependsOn({"sessionIdCookie"})
    public CookieRememberMeManager getCookieRememberMeManager(SimpleCookie simpleCookie) {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }


    /**
     * 这里把 前面两个bean 传入到 manager中
     *
     * @param sessionManager
     * @return
     */
    @Bean("securityManager")
    public SecurityManager securityManager(AdminRealm realm,SessionManager sessionManager,CookieRememberMeManager cookieRememberMeManager ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(realm);
        //验证策略
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(cookieRememberMeManager);
        return securityManager;
    }


    /**
     * Shiro生命周期处理器
     *
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        LifecycleBeanPostProcessor lifecycleBeanPostProcessor = new LifecycleBeanPostProcessor();
        return lifecycleBeanPostProcessor;
    }

    /**
     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }


    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    @DependsOn("securityManager")
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
