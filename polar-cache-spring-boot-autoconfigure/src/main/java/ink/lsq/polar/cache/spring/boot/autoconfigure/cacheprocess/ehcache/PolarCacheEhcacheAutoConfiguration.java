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

package ink.lsq.polar.cache.spring.boot.autoconfigure.cacheprocess.ehcache;

import ink.lsq.polar.cache.ehcache.EhcacheManager;
import ink.lsq.polar.cache.ehcache.EhcacheProperties;
import ink.lsq.polar.cache.ehcache.process.EhcacheCacheAbleProcess;
import ink.lsq.polar.cache.ehcache.process.EhcacheCacheClearProcess;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wdxq liu.shenq@gmail.com
 */
@Configuration
@EnableConfigurationProperties(PolarCacheEhcacheSpringBootProperties.class)
@ConditionalOnClass({EhcacheManager.class, EhcacheCacheAbleProcess.class, EhcacheCacheClearProcess.class})
public class PolarCacheEhcacheAutoConfiguration {

    private final PolarCacheEhcacheSpringBootProperties properties;

    @Autowired
    public PolarCacheEhcacheAutoConfiguration(PolarCacheEhcacheSpringBootProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean(EhcacheManager.class)
    public EhcacheManager ehcacheManager() {
        if (null == properties || StringUtils.isBlank(properties.getXmlFile())) {
            throw new RuntimeException("Ehcache manager init fail, xmlFile not found!");
        }
        EhcacheProperties ehcacheProperties = new EhcacheProperties();
        ehcacheProperties.setXmlFile(properties.getXmlFile());
        EhcacheManager.getInstance().init(ehcacheProperties);
        return EhcacheManager.getInstance();
    }

    @Bean
    @ConditionalOnMissingBean(EhcacheCacheAbleProcess.class)
    public EhcacheCacheAbleProcess ehcacheCacheAbleProcess() {
        return new EhcacheCacheAbleProcess();
    }

    @Bean
    @ConditionalOnMissingBean(EhcacheCacheClearProcess.class)
    public EhcacheCacheClearProcess ehcacheCacheClearProcess() {
        return new EhcacheCacheClearProcess();
    }

}
