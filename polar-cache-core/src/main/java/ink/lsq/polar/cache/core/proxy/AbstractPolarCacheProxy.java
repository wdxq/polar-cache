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

package ink.lsq.polar.cache.core.proxy;

import ink.lsq.polar.cache.core.PolarCacheManager;
import ink.lsq.polar.cache.core.anno.CacheAble;
import ink.lsq.polar.cache.core.anno.CacheClear;
import ink.lsq.polar.cache.core.bean.CacheAbleProcessGetMethodParam;
import ink.lsq.polar.cache.core.bean.CacheAbleProcessPutMethodParam;
import ink.lsq.polar.cache.core.bean.CacheClearProcessParam;
import ink.lsq.polar.cache.core.process.CacheAbleProcess;
import ink.lsq.polar.cache.core.process.CacheClearProcess;
import ink.lsq.polar.cache.core.velocity.VelocityManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public abstract class AbstractPolarCacheProxy implements PolarCacheProxyAbility {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPolarCacheProxy.class);

    private final VelocityManager velocityManager;

    private final List<CacheAbleProcess> cacheAbleProcessList;

    private final List<CacheClearProcess> cacheClearProcessList;

    protected AbstractPolarCacheProxy() {
        this.velocityManager = PolarCacheManager.getInstance().getVelocityManager();
        this.cacheAbleProcessList = PolarCacheManager.getInstance().getCacheAbleProcessList();
        this.cacheClearProcessList = PolarCacheManager.getInstance().getCacheClearProcessList();
    }

    @Override
    public Object cacheAbleProcess(CacheAble cacheAble, Method method, Object[] args, MethodInvokeCallBack action) throws Throwable {

        if (StringUtils.isBlank(cacheAble.value())) {
            throw new CacheNameNotFoundException();
        }

        List<Object> argsList = (args == null || args.length == 0) ? Collections.emptyList() : List.of(args);

        Object result = null;

        if (method.getReturnType() == Void.TYPE) {

            result = action.doInPolarCacheProxy();

            logger.warn("CacheAble is invalid for void method:{}", method.getName());

            return result;

        }

        List<String> cacheKey = null;
        if (StringUtils.isNotBlank(cacheAble.cacheKey())) {
            cacheKey = velocityManager.evaluate(cacheAble.cacheKey(), args);
        }

        for (int i = 0; i < cacheAbleProcessList.size(); i++) {

            result = cacheAbleProcessList.get(i).get(new CacheAbleProcessGetMethodParam(
                    cacheAble.value(),
                    cacheKey,
                    argsList
            ));

            if (null != result) {

                boolean addResult = true;
                for (int j = (i - 1); addResult && j >= 0; j--) {

                    addResult = cacheAbleProcessList.get(j).put(new CacheAbleProcessPutMethodParam(
                            cacheAble.value(),
                            cacheKey,
                            argsList,
                            result
                    ));

                }

                break;

            }

        }

        if (null == result) {

            result = action.doInPolarCacheProxy();

            if (null != result) {
                boolean addResult = true;
                for (int i = cacheAbleProcessList.size() - 1; addResult && i >= 0; i--) {

                    // 执行缓存处理器的添加操作
                    addResult = cacheAbleProcessList.get(i).put(new CacheAbleProcessPutMethodParam(
                            cacheAble.value(),
                            cacheKey,
                            argsList,
                            result
                    ));

                }

            }

        }

        return result;

    }

    @Override
    public void cacheClearProcess(CacheClear cacheClear, Method method, Object[] args, MethodInvokeCallBack action) throws Throwable {

        if (StringUtils.isBlank(cacheClear.value())) {
            throw new CacheNameNotFoundException();
        }

        Object result = null;
        Exception methodCallException = null;

        if (cacheClear.clearWhenExceptionIsThrown()) {
            try {
                result = action.doInPolarCacheProxy();
            } catch (Exception e) {
                methodCallException = e;
            }
        } else {
            result = action.doInPolarCacheProxy();
        }

        if (cacheClear.valBooleanReturn() && !((result instanceof Boolean) && (Boolean) result)) {
            if (null != methodCallException) {
                throw methodCallException;
            }
            return;
        }

        List<Object> argsList = (args == null || args.length == 0) ? Collections.emptyList() : List.of(args);

        List<String> cacheKey = null;
        if (StringUtils.isNotBlank(cacheClear.cacheKey())) {
            cacheKey = velocityManager.evaluate(cacheClear.cacheKey(), args);
        }

        boolean clearResult = true;
        for (int i = cacheClearProcessList.size() - 1; clearResult && i >= 0; i--) {

            clearResult = cacheClearProcessList.get(i).clear(new CacheClearProcessParam(
                    cacheClear.value(),
                    cacheKey,
                    argsList
            ));

        }

        if (null != methodCallException) {
            throw methodCallException;
        }

    }
}
