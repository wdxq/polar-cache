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

import ink.lsq.polar.cache.core.bean.CacheClearProcessParam;
import ink.lsq.polar.cache.core.process.CacheClearProcess;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class EhcacheCacheClearProcess extends AbstractEhcacheProcess implements CacheClearProcess {

    private static final Logger logger = LoggerFactory.getLogger(EhcacheCacheClearProcess.class);

    @SuppressWarnings("unchecked")
    @Override
    public boolean clear(CacheClearProcessParam param) {
        try {

            String cacheName = param.getCacheName();
            Set<String> cacheKey = param.getCacheKey();
            String cacheKeyRegularExpression = param.getCacheKeyRegularExpression();

            Class<?> valueClass = CACHE_VALUE_CLASS_MAP.get(cacheName);
            if (null == valueClass) {
                return true;
            }

            Cache<String, ?> cache = (Cache<String, ?>) cacheManager.getCache(cacheName, CACHE_KEY_TYPE, valueClass);
            if (null == cache) {
                logger.warn(
                        "Ehcache clear fail. Ehcache cache object not found! cacheName:{} keyClass:{}, valueClass:{}",
                        cacheName,
                        CACHE_KEY_TYPE,
                        valueClass
                );
                return false;
            }

            if (param.isClearAll()) {
                cache.clear();
                return true;
            }

            if (null != cacheKey && cacheKey.size() > 0) {
                cache.removeAll(cacheKey);
            }

            if (StringUtils.isNotBlank(cacheKeyRegularExpression)) {
                Set<String> matchCacheKey = new HashSet<>();
                for (Cache.Entry<String, ?> entry : cache) {
                    if (Pattern.matches(cacheKeyRegularExpression, entry.getKey())) {
                        matchCacheKey.add(entry.getKey());
                    }
                }
                if (matchCacheKey.size() > 0) {
                    cache.removeAll(matchCacheKey);
                }
            }

            return true;

        } catch (Exception e) {
            logger.error("Ehcache clear fail. " + e.getMessage(), e);
            return false;
        }
    }
}
