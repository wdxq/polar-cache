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

    public List<String> evaluate(String velocityStr, Object[] args) throws VelocityEvaluateException {
        Map<String, Object> param = new HashMap<>();
        StringWriter out = null;
        try {
            param.put(VelocityConstant.PARAM_ARGS_KEY, args);
            VelocityContext velocityContext = new VelocityContext(param);
            out = new StringWriter();
            Velocity.evaluate(
                    velocityContext,
                    out,
                    VelocityConstant.LOG_TAG,
                    String.format(
                            VelocityConstant.COMMON_PARAM_CACHEKEY_SET_STR,
                            velocityStr
                    )
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
        Object cacheKey = param.get(VelocityConstant.PARAM_CACHEKEY_KEY);
        if (null == cacheKey) {
            throw new VelocityEvaluateException("cacheKey not found. ");
        }
        if (cacheKey instanceof String) {
            return List.of((String) cacheKey);
        }
        if (cacheKey instanceof Collection) {
            List<String> result = new ArrayList<>();
            for (Object currentKey : ((Collection) cacheKey)) {
                if (!(currentKey instanceof String)) {
                    throw new VelocityEvaluateException("cacheKey Type mast be String or Collection<String>. ");
                }
                result.add((String) currentKey);
            }
            return result;
        }
        throw new VelocityEvaluateException("cacheKey Type mast be String or Collection<String>. ");
    }

}
