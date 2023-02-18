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
package io.binghe.rpc.spring.boot.provider.starter;

import io.binghe.rpc.provider.spring.RpcSpringServer;
import io.binghe.rpc.spring.boot.provider.config.SpringBootProviderConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author binghe
 * @version 1.0.0
 * @description RPC 服务提供者的自动配置类
 */
@Configuration
@EnableConfigurationProperties
public class SpringBootProviderAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "bhrpc.binghe.provider")
    public SpringBootProviderConfig springBootProviderConfig(){
        return new SpringBootProviderConfig();
    }

    @Bean
    public RpcSpringServer rpcSpringServer(final SpringBootProviderConfig springBootProviderConfig){
        return new RpcSpringServer(springBootProviderConfig.getServerAddress(),
                springBootProviderConfig.getServerRegistryAddress(),
                springBootProviderConfig.getRegistryAddress(),
                springBootProviderConfig.getRegistryType(),
                springBootProviderConfig.getRegistryLoadBalanceType(),
                springBootProviderConfig.getReflectType(),
                springBootProviderConfig.getHeartbeatInterval(),
                springBootProviderConfig.getScanNotActiveChannelInterval(),
                springBootProviderConfig.getEnableResultCache(),
                springBootProviderConfig.getResultCacheExpire(),
                springBootProviderConfig.getCorePoolSize(),
                springBootProviderConfig.getMaximumPoolSize(),
                springBootProviderConfig.getFlowType(),
                springBootProviderConfig.getMaxConnections(),
                springBootProviderConfig.getDisuseStrategyType(),
                springBootProviderConfig.getEnableBuffer(),
                springBootProviderConfig.getBufferSize(),
                springBootProviderConfig.getEnableRateLimiter(),
                springBootProviderConfig.getRateLimiterType(),
                springBootProviderConfig.getPermits(),
                springBootProviderConfig.getMilliSeconds(),
                springBootProviderConfig.getRateLimiterFailStrategy(),
                springBootProviderConfig.getEnableFusing(),
                springBootProviderConfig.getFusingType(),
                springBootProviderConfig.getTotalFailure(),
                springBootProviderConfig.getFusingMilliSeconds(),
                springBootProviderConfig.getExceptionPostProcessorType());
    }
}
