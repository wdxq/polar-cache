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

/**
 * @author wdxq liu.shenq@gmail.com
 */
public class VelocityConstant {

    public static final String LOG_TAG = "";

    public static final String PARAM_ARGS_KEY = "args";

    public static final String RES_KEY = "res";

    private static final String SET_STR_PREFIX = "#set($" + RES_KEY + "=";

    public static final String STRING_SET_STR = SET_STR_PREFIX + "\"%s\")";

    public static final String OBJECT_SET_STR = SET_STR_PREFIX + "%s)";

}
