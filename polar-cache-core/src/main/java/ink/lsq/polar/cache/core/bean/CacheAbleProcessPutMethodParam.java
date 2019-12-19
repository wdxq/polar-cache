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

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class CacheAbleProcessPutMethodParam extends CacheAbleProcessParam {

    private Object cacheObject;

    public CacheAbleProcessPutMethodParam(String cacheName, List<Object> methodArgs, String cacheKey, Object cacheObject) {
        super(cacheName, methodArgs, cacheKey);
        this.cacheObject = cacheObject;
    }

    public Object getCacheObject() {
        return cacheObject;
    }
}
