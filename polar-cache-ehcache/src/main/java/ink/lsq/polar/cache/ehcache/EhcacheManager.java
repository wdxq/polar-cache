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

package ink.lsq.polar.cache.ehcache;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.xml.XmlConfiguration;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class EhcacheManager {

    private static volatile EhcacheManager instance;

    private CacheManager cacheManager;

    private EhcacheManager() {
    }

    public static EhcacheManager getInstance() {
        if (null == instance) {
            synchronized (EhcacheManager.class) {
                if (null == instance) {
                    instance = new EhcacheManager();
                }
            }
        }
        return instance;
    }

    public void init(EhcacheProperties ehcacheProperties) {
        if (null == ehcacheProperties) {
            throw new RuntimeException("init fail, ehcacheProperties not found!");
        }
        cacheManager = CacheManagerBuilder.newCacheManager(new XmlConfiguration(EhcacheManager.class.getResource(ehcacheProperties.getXmlFile())));
        cacheManager.init();
    }

    public void destroy() {
        if (null != cacheManager) {
            cacheManager.close();
        }
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }
}
