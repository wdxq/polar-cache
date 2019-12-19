/*
 * Copyright 2019 wdxq liu.shenq@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ink.lsq.polar.cache.ehcache.process;

import ink.lsq.polar.cache.core.bean.CacheAbleProcessGetMethodParam;
import ink.lsq.polar.cache.core.bean.CacheAbleProcessPutMethodParam;
import ink.lsq.polar.cache.core.process.CacheAbleProcess;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class EhcacheCacheAbleProcess extends AbstractEhcacheProcess implements CacheAbleProcess {

    private static final Logger logger = LoggerFactory.getLogger(EhcacheCacheAbleProcess.class);

    @SuppressWarnings("unchecked")
    @Override
    public boolean put(CacheAbleProcessPutMethodParam param) {
        try {

            String cacheName = param.getCacheName();
            String cacheKey = param.getCacheKey();
            Object cacheObject = param.getCacheObject();
            Class<?> valueClass = cacheObject.getClass();

            Cache cache = cacheManager.getCache(cacheName, CACHE_KEY_TYPE, valueClass);
            if (null == cache) {
                logger.warn(
                        "Ehcache put fail. Ehcache cache object not found! cacheName:{} keyClass:{}, valueClass:{}",
                        cacheName,
                        CACHE_KEY_TYPE,
                        valueClass
                );
                return false;
            }

            if (StringUtils.isNotBlank(cacheKey)) {
                cache.put(cacheKey, cacheObject);
            } else {
                cache.put(DEFAULT_CACHE_KEY, cacheObject);
            }

            return true;

        } catch (Exception e) {
            logger.error("Ehcache put fail. " + e.getMessage(), e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object get(CacheAbleProcessGetMethodParam param) {
        try {

            String cacheName = param.getCacheName();
            String cacheKey = param.getCacheKey();

            Class<?> valueClass = CACHE_VALUE_CLASS_MAP.get(cacheName);
            if (null == valueClass) {
                return null;
            }

            Cache cache = cacheManager.getCache(cacheName, CACHE_KEY_TYPE, valueClass);
            if (null == cache) {
                logger.warn(
                        "Ehcache get fail. Ehcache cache object not found! cacheName:{} keyClass:{}, valueClass:{}",
                        cacheName,
                        CACHE_KEY_TYPE,
                        valueClass
                );
                return null;
            }

            if (StringUtils.isNotBlank(cacheKey)) {
                return cache.get(cacheKey);
            }

            return cache.get(DEFAULT_CACHE_KEY);

        } catch (Exception e) {
            logger.error("Ehcache get fail. " + e.getMessage(), e);
            return null;
        }
    }
}
