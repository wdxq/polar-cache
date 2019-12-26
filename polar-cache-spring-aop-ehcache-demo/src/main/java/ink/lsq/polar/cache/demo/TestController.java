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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wdxq liu.shenq@gmail.com
 */
@RestController
public class TestController {

    private final TestDao testDao;

    @Autowired
    public TestController(TestDao testDao) {
        this.testDao = testDao;
    }

    @GetMapping("/test")
    public TestResponse test(String param, String param2) {
        return new TestResponse(testDao.testSelect(param, param2));
    }

    @GetMapping("/testClear")
    public TestResponse testClear(String param, String param2) {
        return new TestResponse(String.valueOf(testDao.testClear(param, param2)));
    }

    @GetMapping("/testClearByList")
    public TestResponse testClearByList(String param, String param2) {
        return new TestResponse(String.valueOf(testDao.testClearByList(List.of(param, param2))));
    }

    @GetMapping("/testClearByRegularExpression")
    public TestResponse testClearByRegularExpression(String param) {
        return new TestResponse(String.valueOf(testDao.testClearByRegularExpression(param)));
    }

    @GetMapping("/testClearAll")
    public TestResponse testClearAll() {
        return new TestResponse(String.valueOf(testDao.testClearAll()));
    }

    @GetMapping("/testClearValBooleanReturn")
    public TestResponse testClearValBooleanReturn() {
        return new TestResponse(String.valueOf(testDao.testClearValBooleanReturn()));
    }

    @GetMapping("/testClearWhenExceptionIsThrown")
    public TestResponse testClearWhenExceptionIsThrown() {
        return new TestResponse(String.valueOf(testDao.testClearWhenExceptionIsThrown()));
    }

}
