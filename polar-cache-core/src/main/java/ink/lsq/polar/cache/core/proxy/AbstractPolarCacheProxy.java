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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public abstract class AbstractPolarCacheProxy implements PolarCacheProxyAbility {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPolarCacheProxy.class);

    private VelocityManager velocityManager;

    private List<CacheAbleProcess> cacheAbleProcessList;

    private List<CacheClearProcess> cacheClearProcessList;

    protected AbstractPolarCacheProxy() {
    }

    @Override
    public Object cacheAbleProcess(CacheAble cacheAble, Method method, Object[] args, MethodInvokeCallBack action) throws Throwable {

        if (null == velocityManager) {
            velocityManager = PolarCacheManager.getInstance().getVelocityManager();
        }
        if (null == cacheAbleProcessList) {
            cacheAbleProcessList = PolarCacheManager.getInstance().getCacheAbleProcessList();
        }

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

        String cacheKey = null;
        if (StringUtils.isNotBlank(cacheAble.cacheKey())) {
            cacheKey = velocityManager.evaluateForString(cacheAble.cacheKey(), args);
        }

        for (int i = 0; i < cacheAbleProcessList.size(); i++) {

            result = cacheAbleProcessList.get(i).get(new CacheAbleProcessGetMethodParam(
                    cacheAble.value(),
                    argsList,
                    cacheKey
            ));

            if (null != result) {

                boolean addResult = true;
                for (int j = (i - 1); addResult && j >= 0; j--) {

                    addResult = cacheAbleProcessList.get(j).put(new CacheAbleProcessPutMethodParam(
                            cacheAble.value(),
                            argsList,
                            cacheKey,
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

                    addResult = cacheAbleProcessList.get(i).put(new CacheAbleProcessPutMethodParam(
                            cacheAble.value(),
                            argsList,
                            cacheKey,
                            result
                    ));

                }

            }

        }

        return result;

    }

    @Override
    public Object cacheClearProcess(CacheClear cacheClear, Method method, Object[] args, MethodInvokeCallBack action) throws Throwable {

        if (null == velocityManager) {
            velocityManager = PolarCacheManager.getInstance().getVelocityManager();
        }
        if (null == cacheClearProcessList) {
            cacheClearProcessList = PolarCacheManager.getInstance().getCacheClearProcessList();
        }

        if (StringUtils.isBlank(cacheClear.value())) {
            throw new CacheNameNotFoundException();
        }

        Object result = null;
        Throwable methodCallException = null;

        if (cacheClear.clearWhenExceptionIsThrown().length > 0) {
            try {
                result = action.doInPolarCacheProxy();
            } catch (Throwable e) {
                for (Class<? extends Throwable> aClass : cacheClear.clearWhenExceptionIsThrown()) {
                    if (aClass.isInstance(e)) {
                        methodCallException = e;
                        break;
                    }
                }
                if (methodCallException == null) {
                    throw e;
                }
            }
        } else {
            result = action.doInPolarCacheProxy();
        }

        if (cacheClear.valBooleanReturn() && !((result instanceof Boolean) && (Boolean) result)) {
            if (null != methodCallException) {
                throw methodCallException;
            }
            return result;
        }

        List<Object> argsList = (args == null || args.length == 0) ? Collections.emptyList() : List.of(args);

        Set<String> cacheKey = new HashSet<>();
        String cacheKeyRegularExpression = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(cacheClear.cacheKey())) {
            cacheKey.add(velocityManager.evaluateForString(cacheClear.cacheKey(), args));
        }
        if (StringUtils.isNotBlank(cacheClear.cacheKeyList())) {
            cacheKey.addAll(velocityManager.evaluateForSet(cacheClear.cacheKeyList(), args));
        }
        if (StringUtils.isNotBlank(cacheClear.cacheKeyRegularExpression())) {
            cacheKeyRegularExpression = velocityManager.evaluateForString(cacheClear.cacheKeyRegularExpression(), args);
        }

        boolean clearResult = true;
        for (int i = cacheClearProcessList.size() - 1; clearResult && i >= 0; i--) {

            clearResult = cacheClearProcessList.get(i).clear(new CacheClearProcessParam(
                    cacheClear.value(),
                    argsList,
                    StringUtils.EMPTY.equals(cacheClear.cacheKey())
                            && StringUtils.EMPTY.equals(cacheClear.cacheKeyList())
                            && StringUtils.EMPTY.equals(cacheClear.cacheKeyRegularExpression()),
                    cacheKey,
                    cacheKeyRegularExpression
            ));

        }

        if (null != methodCallException) {
            throw methodCallException;
        }

        return result;

    }
}
