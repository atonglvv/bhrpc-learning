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
package io.binghe.rpc.registry.api;

import io.binghe.rpc.protocol.meta.ServiceMeta;
import io.binghe.rpc.registry.api.config.RegistryConfig;
import io.binghe.rpc.spi.annotation.SPI;

import java.io.IOException;
import java.util.List;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description
 */
@SPI
public interface RegistryService {

    /** 服务注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务取消注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     * @param serviceName 服务名称
     * @param invokerHashCode HashCode值
     * @param sourceIp 源IP地址
     * @return 服务元数据
     * @throws Exception 抛出异常
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode, String sourceIp) throws Exception;

    /**
     * 从多个元数据列表中根据一定的规则获取一个元数据
     * @param serviceMetaList 元数据列表
     * @return 某个特定的元数据
     */
    ServiceMeta select(List<ServiceMeta> serviceMetaList, int invokerHashCode, String sourceIp);


    /**
     * 获取所有的数据
     */
    List<ServiceMeta> discoveryAll() throws Exception;

    /**
     * 服务销毁
     * @throws IOException 抛出异常
     */
    void destroy() throws IOException;

    /**
     * 默认初始化方法
     */
    default void init(RegistryConfig registryConfig) throws Exception {

    }
}
