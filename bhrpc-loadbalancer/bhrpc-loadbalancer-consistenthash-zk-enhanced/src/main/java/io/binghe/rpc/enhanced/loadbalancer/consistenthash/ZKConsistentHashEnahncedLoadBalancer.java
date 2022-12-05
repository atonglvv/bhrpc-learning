/**
 * Copyright 2022-9999 the original author or authors.
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
package io.binghe.rpc.enhanced.loadbalancer.consistenthash;

import io.binghe.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.binghe.rpc.protocol.meta.ServiceMeta;
import io.binghe.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 基于Zookeeper的一致性Hash
 */
@SPIClass
public class ZKConsistentHashEnahncedLoadBalancer implements ServiceLoadBalancer<ServiceMeta> {

    private final static int VIRTUAL_NODE_SIZE = 10;
    private final static String VIRTUAL_NODE_SPLIT = "#";

    private final Logger logger = LoggerFactory.getLogger(ZKConsistentHashEnahncedLoadBalancer.class);

    @Override
    public ServiceMeta select(List<ServiceMeta> servers, int hashCode, String ip) {
        logger.info("基于Zookeeper增强型的一致性Hash算法的负载均衡策略...");
        TreeMap<Integer, ServiceMeta> ring = makeConsistentHashRing(servers);
        return allocateNode(ring, hashCode);
    }

    private ServiceMeta allocateNode(TreeMap<Integer, ServiceMeta> ring, int hashCode) {
        Map.Entry<Integer, ServiceMeta> entry = ring.ceilingEntry(hashCode);
        if (entry == null) {
            entry = ring.firstEntry();
        }
        if (entry == null){
            throw new RuntimeException("not discover useful service, please register service in registry center.");
        }
        return entry.getValue();
    }

    private TreeMap<Integer, ServiceMeta> makeConsistentHashRing(List<ServiceMeta> servers) {
        TreeMap<Integer, ServiceMeta> ring = new TreeMap<>();
        for (ServiceMeta instance : servers) {
            for (int i = 0; i < VIRTUAL_NODE_SIZE; i++) {
                ring.put((buildServiceInstanceKey(instance) + VIRTUAL_NODE_SPLIT + i).hashCode(), instance);
            }
        }
        return ring;
    }

    private String buildServiceInstanceKey(ServiceMeta instance) {
        return String.join(":", instance.getServiceAddr(), String.valueOf(instance.getServicePort()));
    }
}
