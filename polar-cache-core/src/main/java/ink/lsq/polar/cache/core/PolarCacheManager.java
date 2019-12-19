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

package ink.lsq.polar.cache.core;

import ink.lsq.polar.cache.core.process.CacheAbleProcess;
import ink.lsq.polar.cache.core.process.CacheClearProcess;
import ink.lsq.polar.cache.core.velocity.VelocityManager;
import ink.lsq.polar.cache.core.velocity.VelocityProperties;

import java.util.List;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class PolarCacheManager {

    private static volatile PolarCacheManager instance;

    private VelocityManager velocityManager;

    private List<CacheAbleProcess> cacheAbleProcessList;

    private List<CacheClearProcess> cacheClearProcessList;

    private PolarCacheManager() {
    }

    public static PolarCacheManager getInstance() {
        if (null == instance) {
            synchronized (PolarCacheManager.class) {
                if (null == instance) {
                    instance = new PolarCacheManager();
                }
            }
        }
        return instance;
    }

    public void init(VelocityProperties velocityProperties, List<CacheAbleProcess> cacheAbleProcessList, List<CacheClearProcess> cacheClearProcessList) {
        this.velocityManager = new VelocityManager(velocityProperties);
        this.cacheAbleProcessList = cacheAbleProcessList;
        this.cacheClearProcessList = cacheClearProcessList;
    }

    public VelocityManager getVelocityManager() {
        return velocityManager;
    }

    public List<CacheAbleProcess> getCacheAbleProcessList() {
        return cacheAbleProcessList;
    }

    public List<CacheClearProcess> getCacheClearProcessList() {
        return cacheClearProcessList;
    }
}
