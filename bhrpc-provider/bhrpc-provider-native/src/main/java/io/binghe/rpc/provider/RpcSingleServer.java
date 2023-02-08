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
package io.binghe.rpc.provider;

import io.binghe.rpc.provider.common.scanner.RpcServiceScanner;
import io.binghe.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 以Java原生方式启动启动Rpc
 */
public class RpcSingleServer extends BaseServer {

    private final Logger logger = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String serverRegistryAddress, String registryAddress, String registryType,
                           String registryLoadBalanceType, String scanPackage, String reflectType, int heartbeatInterval,
                           int scanNotActiveChannelInterval, boolean enableResultCache, int resultCacheExpire,
                           int corePoolSize, int maximumPoolSize, String flowType, int maxConnections, String disuseStrategyType,
                           boolean enableBuffer, int bufferSize, boolean enableRateLimiter, String rateLimiterType,
                           int permits, int milliSeconds, String rateLimiterFailStrategy) {
        //调用父类构造方法
        super(serverAddress, serverRegistryAddress, registryAddress, registryType, registryLoadBalanceType, reflectType,
                heartbeatInterval, scanNotActiveChannelInterval, enableResultCache, resultCacheExpire, corePoolSize,
                maximumPoolSize, flowType, maxConnections, disuseStrategyType, enableBuffer, bufferSize, enableRateLimiter,
                rateLimiterType, permits, milliSeconds, rateLimiterFailStrategy);
        try {
            this.handlerMap = RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService(this.serverRegistryHost, this.serverRegistryPort, scanPackage, registryService);
        } catch (Exception e) {
            logger.error("RPC Server init error", e);
        }
    }
}
