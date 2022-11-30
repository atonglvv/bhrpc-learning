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
package io.binghe.rpc.enhanced.loadbalancer.random.weight;

import io.binghe.rpc.loadbalancer.base.BaseEnhancedServiceLoadBalancer;
import io.binghe.rpc.protocol.meta.ServiceMeta;
import io.binghe.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * @author binghe(公众号 ： 冰河技术)
 * @version 1.0.0
 * @description 增强型加权随机
 */
@SPIClass
public class RandomWeightServiceEnhancedLoadBalancer extends BaseEnhancedServiceLoadBalancer {
    private final Logger logger = LoggerFactory.getLogger(RandomWeightServiceEnhancedLoadBalancer.class);

    @Override
    public ServiceMeta select(List<ServiceMeta> servers, int hashCode, String ip) {
        logger.info("基于增强型加权随机算法的负载均衡策略...");
        servers = this.getWeightServiceMetaList(servers);
        if (servers == null || servers.isEmpty()){
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
