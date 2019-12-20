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

/**
 * @author wdxq liu.shenq@gmail.com
 */
public enum PolarCacheProcess {

    EHCACHE("ink.lsq.polar.cache.spring.boot.autoconfigure.cacheprocess.ehcache.PolarCacheEhcacheAutoConfiguration");

    private String autoConfigurationClass;

    PolarCacheProcess(String autoConfigurationClass) {
        this.autoConfigurationClass = autoConfigurationClass;
    }

    public String getAutoConfigurationClass() {
        return autoConfigurationClass;
    }
}
