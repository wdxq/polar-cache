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

package ink.lsq.polar.cache.core.bean;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public abstract class CacheAbleProcessParam extends BaseCacheProcessParam {

    private Object cacheKey;

    private long expireTime;

    private TimeUnit expireTimeUnit;

    public CacheAbleProcessParam(String cacheName, List<Object> methodArgs, Object cacheKey, long expireTime, TimeUnit expireTimeUnit) {
        super(cacheName, methodArgs);
        this.cacheKey = cacheKey;
        this.expireTime = expireTime;
        this.expireTimeUnit = expireTimeUnit;
    }

    public Object getCacheKey() {
        return cacheKey;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public TimeUnit getExpireTimeUnit() {
        return expireTimeUnit;
    }
}
