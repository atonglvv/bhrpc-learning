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
package io.binghe.rpc.consumer;

import io.binghe.rpc.consumer.common.RpcConsumer;
import io.binghe.rpc.proxy.api.async.IAsyncObjectProxy;
import io.binghe.rpc.proxy.api.object.ObjectProxy;
import io.binghe.rpc.proxy.jdk.JdkProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 服务消费客户端
 */
public class RpcClient {

    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 序列化类型
     */
    private String serializationType;
    /**
     * 超时时间
     */
    private long timeout;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public RpcClient(String serviceVersion, String serviceGroup, String serializationType, long timeout, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public <T> T create(Class<T> interfaceClass) {
        JdkProxyFactory<T> jdkProxyFactory = new JdkProxyFactory<T>(serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway);
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {
        return new ObjectProxy<T>(interfaceClass, serviceVersion, serviceGroup, serializationType, timeout, RpcConsumer.getInstance(), async, oneway);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }
}
