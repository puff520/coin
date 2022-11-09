package com.aqmd.netty.shiro;

import com.aqmd.netty.filter.HFilter;
import com.aqmd.netty.shiro.filter.AbstractShiroFilter;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.shiro.config.Ini;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class HawkShiroFilterFactoryBean implements FactoryBean, BeanPostProcessor {
   private static final transient Logger log = LoggerFactory.getLogger(HawkShiroFilterFactoryBean.class);
   private SecurityManager securityManager;
   private Map<String, HFilter> filters = new LinkedHashMap();
   private Map<String, String> filterChainDefinitionMap = new LinkedHashMap();
   private AbstractShiroFilter instance;

   public SecurityManager getSecurityManager() {
      return this.securityManager;
   }

   public void setSecurityManager(SecurityManager securityManager) {
      this.securityManager = securityManager;
   }

   public Object getObject() throws Exception {
      if (this.instance == null) {
         this.instance = this.createInstance();
      }

      return this.instance;
   }

   public Class getObjectType() {
      return SpringShiroFilter.class;
   }

   public boolean isSingleton() {
      return true;
   }

   protected AbstractShiroFilter createInstance() throws Exception {
      log.debug("Creating Shiro Filter instance.");
      SecurityManager securityManager = this.getSecurityManager();
      if (securityManager == null) {
         String msg = "SecurityManager property must be set.";
         throw new BeanInitializationException(msg);
      } else {
         return new SpringShiroFilter(securityManager);
      }
   }

   public void setFilterChainDefinitions(String definitions) {
      Ini ini = new Ini();
      ini.load(definitions);
      Ini.Section section = ini.getSection("urls");
      if (CollectionUtils.isEmpty(section)) {
         section = ini.getSection("");
      }

      this.setFilterChainDefinitionMap(section);
   }

   public Map<String, HFilter> getFilters() {
      return this.filters;
   }

   public void setFilters(Map<String, HFilter> filters) {
      this.filters = filters;
   }

   public Map<String, String> getFilterChainDefinitionMap() {
      return this.filterChainDefinitionMap;
   }

   public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
      this.filterChainDefinitionMap = filterChainDefinitionMap;
   }

   public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
      return bean;
   }

   public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      return bean;
   }

   private static final class SpringShiroFilter extends AbstractShiroFilter {
      protected SpringShiroFilter(SecurityManager webSecurityManager) {
         if (webSecurityManager == null) {
            throw new IllegalArgumentException("WebSecurityManager property cannot be null.");
         } else {
            this.setSecurityManager(webSecurityManager);
         }
      }
   }
}
