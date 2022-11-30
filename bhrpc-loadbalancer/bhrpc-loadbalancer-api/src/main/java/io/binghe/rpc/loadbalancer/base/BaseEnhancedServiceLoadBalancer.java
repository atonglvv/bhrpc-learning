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
package io.binghe.rpc.loadbalancer.base;

import io.binghe.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.binghe.rpc.protocol.meta.ServiceMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 基础的增强型负载均衡类
 */
public abstract class BaseEnhancedServiceLoadBalancer implements ServiceLoadBalancer<ServiceMeta> {

    /**
     * 根据权重重新生成服务元数据列表，权重越高的元数据，会在最终的列表中出现的次数越多
     * 例如，权重为1，最终出现1次，权重为2，最终出现2次，权重为3，最终出现3次，依此类推...
     */
    protected List<ServiceMeta> getWeightServiceMetaList(List<ServiceMeta> servers){
        if (servers == null || servers.isEmpty()){
            return null;
        }
        List<ServiceMeta> serviceMetaList = new ArrayList<>();
        servers.stream().forEach((server) -> {
            IntStream.range(0, server.getWeight()).forEach((i) -> {
                serviceMetaList.add(server);
            });
        });
        return serviceMetaList;
    }
}
