/**
 * Copyright 2020-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.binghe.rpc.common.scanner.reference;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author binghe
 * @version 1.0.0
 * @description 存在@RpcReference注解字段代理实例的上下文
 */
public class RpcReferenceContext {

    private static volatile Map<String, Object>  instance;

    static {
        instance = new ConcurrentHashMap<>();
    }

    public static void put(String key, Object value){
        instance.put(key, value);
    }

    public static Object get(String key){
        return instance.get(key);
    }

    public static Object remove(String key){
        return instance.remove(key);
    }
}
