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

package ink.lsq.polar.cache.core.velocity;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class VelocityManager {

    private static final Logger logger = LoggerFactory.getLogger(VelocityManager.class);

    public VelocityManager(VelocityProperties velocityProperties) {
        if (null == velocityProperties) {
            throw new RuntimeException("init fail, velocityProperties not found!");
        }
        try {
            Velocity.setProperty(Velocity.RESOURCE_LOADER, "string");
            Velocity.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
            Velocity.setProperty(Velocity.PARSER_POOL_SIZE, velocityProperties.getParserPoolSize());
            Velocity.init();
            if (logger.isInfoEnabled()) {
                logger.info("Velocity init success.");
            }
        } catch (Exception e) {
            logger.error("Velocity init fail. " + e.getMessage(), e);
        }
    }

    public String evaluateForString(String velocityStr, Object[] args) throws VelocityEvaluateException {
        Object cacheKey = evaluate(String.format(VelocityConstant.STRING_SET_STR, velocityStr), args);
        if (cacheKey instanceof String) {
            return (String) cacheKey;
        } else {
            throw new VelocityEvaluateException("cacheKey Type mast be String. ");
        }
    }

    public Set<String> evaluateForSet(String velocityStr, Object[] args) throws VelocityEvaluateException {
        Object cacheKeyList = evaluate(String.format(VelocityConstant.OBJECT_SET_STR, velocityStr), args);
        if (cacheKeyList instanceof Collection) {
            Collection collection = (Collection) cacheKeyList;
            Set<String> result = new HashSet<>(collection.size());
            for (Object currentKey : collection) {
                result.add(currentKey.toString());
            }
            return result;
        } else {
            throw new VelocityEvaluateException("cacheKeyList Type mast be Collection<Object with toString method>. ");
        }
    }

    private Object evaluate(String velocityStr, Object[] args) {
        Map<String, Object> param = new HashMap<>();
        StringWriter out = null;
        try {
            Object[] copyArgs;
            if (null != args && args.length > 0) {
                copyArgs = new Object[args.length];
                System.arraycopy(args, 0, copyArgs, 0, args.length);
                for (int i = 0; i < copyArgs.length; i++) {
                    if (copyArgs[i] == null) {
                        copyArgs[i] = StringUtils.EMPTY;
                    }
                }
            } else {
                copyArgs = args;
            }
            param.put(VelocityConstant.PARAM_ARGS_KEY, copyArgs);
            VelocityContext velocityContext = new VelocityContext(param);
            out = new StringWriter();
            Velocity.evaluate(
                    velocityContext,
                    out,
                    VelocityConstant.LOG_TAG,
                    velocityStr
            );
        } finally {
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("Velocity StringWriter close fail. " + e.getMessage(), e);
                }
            }
        }
        return param.get(VelocityConstant.RES_KEY);
    }

}
