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

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class PolarCacheRegistrar implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> annotationAttributes = importingClassMetadata.getAnnotationAttributes(EnablePolarCache.class.getName());
        if (null == annotationAttributes) {
            throw new RuntimeException("EnablePolarCache annotation attribute missing.");
        }
        PolarCacheProxy proxy = (PolarCacheProxy) annotationAttributes.get("proxy");
        if (null == proxy) {
            throw new RuntimeException("EnablePolarCache annotation proxy attribute missing.");
        }
        PolarCacheProcess[] process = (PolarCacheProcess[]) annotationAttributes.get("process");
        if (null == process || process.length == 0) {
            throw new RuntimeException("EnablePolarCache annotation process attribute missing.");
        }
        String[] autoConfigurationClass = new String[process.length + 2];
        for (int i = 0; i < process.length; i++) {
            autoConfigurationClass[i] = process[i].getAutoConfigurationClass();
        }
        autoConfigurationClass[process.length] = proxy.getAutoConfigurationClass();
        autoConfigurationClass[process.length + 1] = "ink.lsq.polar.cache.spring.boot.autoconfigure.PolarCacheManagerAutoConfiguration";
        return autoConfigurationClass;
    }
}
