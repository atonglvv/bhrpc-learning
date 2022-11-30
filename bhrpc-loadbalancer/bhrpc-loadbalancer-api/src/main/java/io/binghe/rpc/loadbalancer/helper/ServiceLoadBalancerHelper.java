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
package io.binghe.rpc.loadbalancer.helper;

import io.binghe.rpc.protocol.meta.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 服务负载均衡辅助类
 */
public class ServiceLoadBalancerHelper {

    private static volatile List<ServiceMeta> cacheServiceMeta = new CopyOnWriteArrayList<>();

    public static List<ServiceMeta> getServiceMetaList(List<ServiceInstance<ServiceMeta>> serviceInstances){
        if (serviceInstances == null || serviceInstances.isEmpty() || cacheServiceMeta.size() == serviceInstances.size()){
            return cacheServiceMeta;
        }
        //先清空cacheServiceMeta中的数据
        cacheServiceMeta.clear();
        serviceInstances.stream().forEach((serviceMetaServiceInstance) -> {
            cacheServiceMeta.add(serviceMetaServiceInstance.getPayload());
        });
        return cacheServiceMeta;
    }
}
