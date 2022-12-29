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
package io.binghe.rpc.spring.boot.consumer.starter;

import io.binghe.rpc.consumer.RpcClient;
import io.binghe.rpc.spring.boot.consumer.config.SpringBootConsumerConfig;
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
public class SpringBootConsumerAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "bhrpc.binghe.consumer")
    public SpringBootConsumerConfig springBootConsumerConfig(){
        return new SpringBootConsumerConfig();
    }

    @Bean
    public RpcClient rpcClient(final SpringBootConsumerConfig springBootConsumerConfig){
        return new RpcClient(springBootConsumerConfig.getRegistryAddress(),
                springBootConsumerConfig.getRegistryType(),
                springBootConsumerConfig.getLoadBalanceType(),
                springBootConsumerConfig.getProxy(),
                springBootConsumerConfig.getVersion(),
                springBootConsumerConfig.getGroup(),
                springBootConsumerConfig.getSerializationType(),
                springBootConsumerConfig.getTimeout(),
                springBootConsumerConfig.getAsync(),
                springBootConsumerConfig.getOneway(),
                springBootConsumerConfig.getHeartbeatInterval(),
                springBootConsumerConfig.getScanNotActiveChannelInterval(),
                springBootConsumerConfig.getRetryInterval(),
                springBootConsumerConfig.getRetryTimes());
    }
}
