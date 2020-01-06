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

package ink.lsq.polar.cache.proxy.spring.aop;

import ink.lsq.polar.cache.core.anno.CacheAble;
import ink.lsq.polar.cache.core.anno.CacheClear;
import ink.lsq.polar.cache.core.anno.CacheClearBatch;
import ink.lsq.polar.cache.core.proxy.AbstractPolarCacheProxy;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author wdxq liu.shenq@gmail.com
 */
@Aspect
public class PolarCacheAspect extends AbstractPolarCacheProxy {

    @Around("@annotation(cacheAble)")
    public Object cacheAble(final ProceedingJoinPoint proceedingJoinPoint, CacheAble cacheAble) throws Throwable {

        return cacheAbleProcess(
                cacheAble,
                ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod(),
                proceedingJoinPoint.getArgs(),
                () -> proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs())
        );

    }

    @Around("@annotation(cacheClear)")
    public Object cacheClear(ProceedingJoinPoint proceedingJoinPoint, CacheClear cacheClear) throws Throwable {

        return cacheClearProcess(
                new CacheClear[]{cacheClear},
                ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod(),
                proceedingJoinPoint.getArgs(),
                () -> proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs())
        );

    }

    @Around("@annotation(cacheClearBatch)")
    public Object cacheClearBatch(ProceedingJoinPoint proceedingJoinPoint, CacheClearBatch cacheClearBatch) throws Throwable {

        return cacheClearProcess(
                cacheClearBatch.value(),
                ((MethodSignature) proceedingJoinPoint.getSignature()).getMethod(),
                proceedingJoinPoint.getArgs(),
                () -> proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs())
        );

    }

}
