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
package io.binghe.rpc.loadbalancer.context;

import io.binghe.rpc.protocol.meta.ServiceMeta;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 连接数上下文
 */
public class ConnectionsContext {

    private static volatile Map<String, Integer> connectionsMap = new ConcurrentHashMap<>();

    public static void add(ServiceMeta serviceMeta){
        String key = generateKey(serviceMeta);
        Integer count = connectionsMap.get(key);
        if (count == null){
            count = 0;
        }
        count++;
        connectionsMap.put(key, count);
    }

    public static Integer getValue(ServiceMeta serviceMeta){
        String key = generateKey(serviceMeta);
        return connectionsMap.get(key);
    }

    private static String generateKey(ServiceMeta serviceMeta){
        return serviceMeta.getServiceAddr().concat(":").concat(String.valueOf(serviceMeta.getServicePort()));
    }
}
