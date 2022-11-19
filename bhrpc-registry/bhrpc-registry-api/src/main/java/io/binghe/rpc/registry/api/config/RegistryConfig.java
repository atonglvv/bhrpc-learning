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
package io.binghe.rpc.registry.api.config;

import java.io.Serializable;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 注册配置类
 */
public class RegistryConfig implements Serializable {
    private static final long serialVersionUID = -7248658103788758893L;

    /**
     * 注册地址
     */
    private String registryAddr;

    /**
     * 注册类型
     */
    private String registryType;

    /**
     * 负载均衡类型
     */
    private String registryLoadBalanceType;


    public RegistryConfig(String registryAddr, String registryType, String registryLoadBalanceType) {
        this.registryAddr = registryAddr;
        this.registryType = registryType;
        this.registryLoadBalanceType = registryLoadBalanceType;
    }

    public String getRegistryAddr() {
        return registryAddr;
    }

    public void setRegistryAddr(String registryAddr) {
        this.registryAddr = registryAddr;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public String getRegistryLoadBalanceType() {
        return registryLoadBalanceType;
    }

    public void setRegistryLoadBalanceType(String registryLoadBalanceType) {
        this.registryLoadBalanceType = registryLoadBalanceType;
    }
}
