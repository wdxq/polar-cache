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

package ink.lsq.polar.cache.demo;

import ink.lsq.polar.cache.core.anno.CacheAble;
import ink.lsq.polar.cache.core.anno.CacheClear;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wdxq liu.shenq@gmail.com
 */
@Repository
public class TestDao {

    @CacheAble(value = "DemoCache", cacheKey = "$args[0]-$args[1]")
    public String testSelect(String param, String param2) {
        return "Hello World! " + param + param2;
    }

    @CacheClear(value = "DemoCache", cacheKey = "$args[0]-$args[1]")
    public boolean testClear(String param, String param2) {
        return true;
    }

    @CacheClear(value = "DemoCache", cacheKeyList = "$args[0]")
    public boolean testClearByList(List<String> param) {
        return true;
    }

    @CacheClear(value = "DemoCache", cacheKeyRegularExpression = "$args[0].*")
    public boolean testClearByRegularExpression(String param, String param2) {
        return true;
    }

}
