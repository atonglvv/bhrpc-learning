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
package io.binghe.rpc.loadbalancer.api;

import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.spi.annotation.SPI;

import java.util.List;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 负载均衡接口
 */
@SPI(RpcConstants.SERVICE_LOAD_BALANCER_RANDOM)
public interface ServiceLoadBalancer<T> {
    /**
     * 以负载均衡的方式选取一个服务节点
     * @param servers 服务列表
     * @param hashCode Hash值
     * @return 可用的服务节点
     */
    T select(List<T> servers, int hashCode);
}
