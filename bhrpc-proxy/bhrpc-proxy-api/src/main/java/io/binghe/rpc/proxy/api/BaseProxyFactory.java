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
package io.binghe.rpc.proxy.api;

import io.binghe.rpc.proxy.api.object.ObjectProxy;
import io.binghe.rpc.proxy.api.config.ProxyConfig;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 基础代理工厂类
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {

    protected ObjectProxy<T> objectProxy;

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {
        this.objectProxy = new ObjectProxy(proxyConfig.getClazz(),
                proxyConfig.getServiceVersion(),
                proxyConfig.getServiceGroup(),
                proxyConfig.getSerializationType(),
                proxyConfig.getTimeout(),
                proxyConfig.getRegistryService(),
                proxyConfig.getConsumer(),
                proxyConfig.getAsync(),
                proxyConfig.getOneway(),
                proxyConfig.getEnableResultCache(),
                proxyConfig.getResultCacheExpire(),
                proxyConfig.getReflectType(),
                proxyConfig.getFallbackClassName(),
                proxyConfig.getFallbackClass(),
                proxyConfig.getEnableRateLimiter(),
                proxyConfig.getRateLimiterType(),
                proxyConfig.getPermits(),
                proxyConfig.getMilliSeconds(),
                proxyConfig.getRateLimiterFailStrategy(),
                proxyConfig.getEnableFusing(),
                proxyConfig.getFusingType(),
                proxyConfig.getTotalFailure(),
                proxyConfig.getFusingMilliSeconds(),
                proxyConfig.getExceptionPostProcessorType());
    }
}
