package com.aqmd.netty.shiro.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.sf.ehcache.Ehcache;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.cache.support.SimpleValueWrapper;

public class SpringCacheManagerWrapper implements CacheManager {
   private org.springframework.cache.CacheManager cacheManager;

   public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
      this.cacheManager = cacheManager;
   }

   public <K, V> Cache<K, V> getCache(String name) throws CacheException {
      org.springframework.cache.Cache springCache = this.cacheManager.getCache(name);
      return new SpringCacheWrapper(springCache);
   }

   static class SpringCacheWrapper implements Cache {
      private org.springframework.cache.Cache springCache;

      SpringCacheWrapper(org.springframework.cache.Cache springCache) {
         this.springCache = springCache;
      }

      public Object get(Object key) throws CacheException {
         Object value = this.springCache.get(key);
         return value instanceof SimpleValueWrapper ? ((SimpleValueWrapper)value).get() : value;
      }

      public Object put(Object key, Object value) throws CacheException {
         this.springCache.put(key, value);
         return value;
      }

      public Object remove(Object key) throws CacheException {
         this.springCache.evict(key);
         return null;
      }

      public void clear() throws CacheException {
         this.springCache.clear();
      }

      public int size() {
         if (this.springCache.getNativeCache() instanceof Ehcache) {
            Ehcache ehcache = (Ehcache)this.springCache.getNativeCache();
            return ehcache.getSize();
         } else {
            throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");
         }
      }

      public Set keys() {
         if (this.springCache.getNativeCache() instanceof Ehcache) {
            Ehcache ehcache = (Ehcache)this.springCache.getNativeCache();
            return new HashSet(ehcache.getKeys());
         } else {
            throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
         }
      }

      public Collection values() {
         if (this.springCache.getNativeCache() instanceof Ehcache) {
            Ehcache ehcache = (Ehcache)this.springCache.getNativeCache();
            List keys = ehcache.getKeys();
            if (!CollectionUtils.isEmpty(keys)) {
               List values = new ArrayList(keys.size());
               this.addValue(keys, values);
               return Collections.unmodifiableList(values);
            } else {
               return Collections.emptyList();
            }
         } else {
            throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
         }
      }

      public void addValue(List keys, List values) {
         Iterator var3 = keys.iterator();

         while(var3.hasNext()) {
            Object key = var3.next();
            Object value = this.get(key);
            if (value != null) {
               values.add(value);
            }
         }

      }
   }
}
