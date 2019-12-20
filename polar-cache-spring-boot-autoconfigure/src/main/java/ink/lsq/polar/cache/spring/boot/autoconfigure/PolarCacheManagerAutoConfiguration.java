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

package ink.lsq.polar.cache.spring.boot.autoconfigure;

import ink.lsq.polar.cache.core.PolarCacheManager;
import ink.lsq.polar.cache.core.process.CacheAbleProcess;
import ink.lsq.polar.cache.core.process.CacheClearProcess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author wdxq liu.shenq@gmail.com
 */
@Configuration
@EnableConfigurationProperties(PolarCacheManagerSpringBootProperties.class)
@ConditionalOnClass(PolarCacheManager.class)
public class PolarCacheManagerAutoConfiguration {

    private final PolarCacheManagerSpringBootProperties properties;

    private final List<CacheAbleProcess> cacheAbleProcessList;

    private final List<CacheClearProcess> cacheClearProcessList;

    @Autowired
    public PolarCacheManagerAutoConfiguration(
            PolarCacheManagerSpringBootProperties properties,
            List<CacheAbleProcess> cacheAbleProcessList,
            List<CacheClearProcess> cacheClearProcessList
    ) {
        this.properties = properties;
        this.cacheAbleProcessList = cacheAbleProcessList;
        this.cacheClearProcessList = cacheClearProcessList;
    }

    @Bean
    @ConditionalOnMissingBean(PolarCacheManager.class)
    public PolarCacheManager polarCacheManager() {
        PolarCacheManager.getInstance().init(properties.getVelocity(), cacheAbleProcessList, cacheClearProcessList);
        return PolarCacheManager.getInstance();
    }

}
