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

import ink.lsq.polar.cache.ehcache.EhcacheManager;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.CacheManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public abstract class AbstractEhcacheProcess {

    protected static final String DEFAULT_CACHE_KEY = StringUtils.EMPTY;

    protected static final Class<?> CACHE_KEY_TYPE = String.class;

    protected static final Map<String, Class<?>> CACHE_VALUE_CLASS_MAP = new ConcurrentHashMap<>();

    protected CacheManager cacheManager;

    public AbstractEhcacheProcess() {
        if (null == EhcacheManager.getInstance().getCacheManager()) {
            throw new RuntimeException("CacheManager not found!");
        }
        this.cacheManager = EhcacheManager.getInstance().getCacheManager();
    }

}
