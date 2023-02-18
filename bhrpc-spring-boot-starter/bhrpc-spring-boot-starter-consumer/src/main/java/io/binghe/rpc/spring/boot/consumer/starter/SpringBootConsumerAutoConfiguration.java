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

import io.binghe.rpc.common.utils.StringUtils;
import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.consumer.RpcClient;
import io.binghe.rpc.consumer.spring.RpcReferenceBean;
import io.binghe.rpc.consumer.spring.context.RpcConsumerSpringContext;
import io.binghe.rpc.spring.boot.consumer.config.SpringBootConsumerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author binghe
 * @version 1.0.0
 * @description RPC 服务提供者的自动配置类，配置优先级：服务消费者字段上配置的@RpcReference注解属性 > yml文件 > @RpcReference默认注解属性
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
    public List<RpcClient> rpcClient(final SpringBootConsumerConfig springBootConsumerConfig){
        return parseRpcClient(springBootConsumerConfig);
    }

    private List<RpcClient> parseRpcClient(final SpringBootConsumerConfig springBootConsumerConfig){
        List<RpcClient> rpcClientList = new ArrayList<>();
        ApplicationContext context = RpcConsumerSpringContext.getInstance().getContext();
        Map<String, RpcReferenceBean> rpcReferenceBeanMap = context.getBeansOfType(RpcReferenceBean.class);
        Collection<RpcReferenceBean> rpcReferenceBeans = rpcReferenceBeanMap.values();
        for (RpcReferenceBean rpcReferenceBean : rpcReferenceBeans){
            rpcReferenceBean = this.getRpcReferenceBean(rpcReferenceBean, springBootConsumerConfig);
            rpcReferenceBean.init();
            rpcClientList.add(rpcReferenceBean.getRpcClient());
        }
        return rpcClientList;
    }

    /**
     * 首先从Spring IOC容器中获取RpcReferenceBean，
     * 如果存在RpcReferenceBean，部分RpcReferenceBean的字段为空，则使用springBootConsumerConfig字段进行填充
     * 如果不存在RpcReferenceBean，则使用springBootConsumerConfig构建RpcReferenceBean
     */
    private RpcReferenceBean getRpcReferenceBean(final RpcReferenceBean referenceBean, final SpringBootConsumerConfig springBootConsumerConfig){
        if (StringUtils.isEmpty(referenceBean.getGroup())
                || (RpcConstants.RPC_COMMON_DEFAULT_GROUP.equals(referenceBean.getGroup()) && !StringUtils.isEmpty(springBootConsumerConfig.getGroup()))){
            referenceBean.setGroup(springBootConsumerConfig.getGroup());
        }
        if (StringUtils.isEmpty(referenceBean.getVersion())
                || (RpcConstants.RPC_COMMON_DEFAULT_VERSION.equals(referenceBean.getVersion()) && !StringUtils.isEmpty(springBootConsumerConfig.getVersion()))){
            referenceBean.setVersion(springBootConsumerConfig.getVersion());
        }
        if (StringUtils.isEmpty(referenceBean.getRegistryType())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_REGISTRYTYPE.equals(referenceBean.getRegistryType()) && !StringUtils.isEmpty(springBootConsumerConfig.getRegistryType()))){
            referenceBean.setRegistryType(springBootConsumerConfig.getRegistryType());
        }
        if (StringUtils.isEmpty(referenceBean.getLoadBalanceType())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_LOADBALANCETYPE.equals(referenceBean.getLoadBalanceType()) && !StringUtils.isEmpty(springBootConsumerConfig.getLoadBalanceType()))){
            referenceBean.setLoadBalanceType(springBootConsumerConfig.getLoadBalanceType());
        }
        if (StringUtils.isEmpty(referenceBean.getSerializationType())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_SERIALIZATIONTYPE.equals(referenceBean.getSerializationType()) && !StringUtils.isEmpty(springBootConsumerConfig.getSerializationType()))){
            referenceBean.setSerializationType(springBootConsumerConfig.getSerializationType());
        }
        if (StringUtils.isEmpty(referenceBean.getRegistryAddress())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_REGISTRYADDRESS.equals(referenceBean.getRegistryAddress()) && !StringUtils.isEmpty(springBootConsumerConfig.getRegistryAddress()))){
            referenceBean.setRegistryAddress(springBootConsumerConfig.getRegistryAddress());
        }
        if (referenceBean.getTimeout() <= 0
                || (RpcConstants.RPC_REFERENCE_DEFAULT_TIMEOUT == referenceBean.getTimeout() && springBootConsumerConfig.getTimeout() > 0)){
            referenceBean.setTimeout(springBootConsumerConfig.getTimeout());
        }
        if (!referenceBean.isAsync()){
            referenceBean.setAsync(springBootConsumerConfig().getAsync());
        }
        if (!referenceBean.isOneway()){
            referenceBean.setOneway(springBootConsumerConfig().getOneway());
        }
        if (StringUtils.isEmpty(referenceBean.getProxy())
                || (RpcConstants.RPC_REFERENCE_DEFAULT_PROXY.equals(referenceBean.getProxy()) && !StringUtils.isEmpty(springBootConsumerConfig.getProxy()) )){
            referenceBean.setProxy(springBootConsumerConfig.getProxy());
        }
        if (referenceBean.getHeartbeatInterval() <= 0
                || (RpcConstants.RPC_COMMON_DEFAULT_HEARTBEATINTERVAL == referenceBean.getHeartbeatInterval() && springBootConsumerConfig.getHeartbeatInterval() > 0 )){
            referenceBean.setHeartbeatInterval(springBootConsumerConfig.getHeartbeatInterval());
        }
        if (referenceBean.getRetryInterval() <= 0
                || (RpcConstants.RPC_REFERENCE_DEFAULT_RETRYINTERVAL == referenceBean.getRetryInterval() && springBootConsumerConfig.getRetryInterval() > 0)){
            referenceBean.setRetryInterval(springBootConsumerConfig.getRetryInterval());
        }
        if (referenceBean.getRetryTimes() <= 0
                || (RpcConstants.RPC_REFERENCE_DEFAULT_RETRYTIMES == referenceBean.getRetryTimes() && springBootConsumerConfig.getRetryTimes() > 0)){
            referenceBean.setRetryTimes(springBootConsumerConfig.getRetryTimes());
        }
        if (referenceBean.getScanNotActiveChannelInterval() <= 0
                || (RpcConstants.RPC_COMMON_DEFAULT_SCANNOTACTIVECHANNELINTERVAL == referenceBean.getScanNotActiveChannelInterval() && springBootConsumerConfig.getScanNotActiveChannelInterval() > 0)){
            referenceBean.setScanNotActiveChannelInterval(springBootConsumerConfig().getScanNotActiveChannelInterval());
        }
        if (!referenceBean.isEnableResultCache()){
            referenceBean.setEnableResultCache(springBootConsumerConfig.getEnableResultCache());
        }
        if (referenceBean.getResultCacheExpire() <= 0
                || (RpcConstants.RPC_SCAN_RESULT_CACHE_EXPIRE == referenceBean.getResultCacheExpire() && springBootConsumerConfig.getResultCacheExpire() > 0)){
            referenceBean.setResultCacheExpire(springBootConsumerConfig.getResultCacheExpire());
        }

        if (!referenceBean.isEnableDirectServer()){
            referenceBean.setEnableDirectServer(springBootConsumerConfig.getEnableDirectServer());
        }

        if (StringUtils.isEmpty(referenceBean.getDirectServerUrl())
                || (RpcConstants.RPC_COMMON_DEFAULT_DIRECT_SERVER.equals(referenceBean.getDirectServerUrl()) && !StringUtils.isEmpty(springBootConsumerConfig.getDirectServerUrl()))){
            referenceBean.setDirectServerUrl(springBootConsumerConfig.getDirectServerUrl());

        }

        if (!referenceBean.isEnableDelayConnection()){
            referenceBean.setEnableDelayConnection(springBootConsumerConfig.getEnableDelayConnection());
        }

        if (referenceBean.getCorePoolSize() <= 0
                || (RpcConstants.DEFAULT_CORE_POOL_SIZE == referenceBean.getCorePoolSize() && springBootConsumerConfig.getCorePoolSize() > 0)){
            referenceBean.setCorePoolSize(springBootConsumerConfig.getCorePoolSize());
        }

        if (referenceBean.getMaximumPoolSize() <= 0
                || (RpcConstants.DEFAULT_MAXI_NUM_POOL_SIZE == referenceBean.getMaximumPoolSize() && springBootConsumerConfig.getMaximumPoolSize() > 0)){
            referenceBean.setMaximumPoolSize(springBootConsumerConfig.getMaximumPoolSize());
        }

        if (StringUtils.isEmpty(referenceBean.getFlowType())
                || (RpcConstants.FLOW_POST_PROCESSOR_PRINT.equals(referenceBean.getFlowType()) && !StringUtils.isEmpty(springBootConsumerConfig.getFlowType()))){
            referenceBean.setFlowType(springBootConsumerConfig.getFlowType());
        }

        if (!referenceBean.isEnableBuffer()){
            referenceBean.setEnableBuffer(springBootConsumerConfig.getEnableBuffer());
        }

        if (referenceBean.getBufferSize() <= 0
                || (RpcConstants.DEFAULT_BUFFER_SIZE == referenceBean.getBufferSize() && springBootConsumerConfig.getBufferSize() > 0)){
            referenceBean.setBufferSize(springBootConsumerConfig.getBufferSize());
        }

        if (StringUtils.isEmpty(referenceBean.getReflectType())
                || (RpcConstants.DEFAULT_REFLECT_TYPE.equals(referenceBean.getReflectType()) && !StringUtils.isEmpty(springBootConsumerConfig.getReflectType()))){
            referenceBean.setReflectType(springBootConsumerConfig.getReflectType());
        }

        if (StringUtils.isEmpty(referenceBean.getFallbackClassName())
                || (RpcConstants.DEFAULT_FALLBACK_CLASS_NAME.equals(referenceBean.getFallbackClassName()) && !StringUtils.isEmpty(springBootConsumerConfig.getFallbackClassName()))){
            referenceBean.setFallbackClassName(springBootConsumerConfig.getFallbackClassName());
        }

        if (!referenceBean.isEnableRateLimiter()){
            referenceBean.setEnableRateLimiter(springBootConsumerConfig.getEnableRateLimiter());
        }

        if (StringUtils.isEmpty(referenceBean.getRateLimiterType())
                || (RpcConstants.DEFAULT_RATELIMITER_INVOKER.equals(referenceBean.getRateLimiterType()) && !StringUtils.isEmpty(springBootConsumerConfig.getRateLimiterType()))){
            referenceBean.setRateLimiterType(springBootConsumerConfig.getRateLimiterType());
        }

        if (referenceBean.getPermits() <= 0
                || (RpcConstants.DEFAULT_RATELIMITER_PERMITS == referenceBean.getPermits() && springBootConsumerConfig.getPermits() > 0)){
            referenceBean.setPermits(springBootConsumerConfig.getPermits());
        }

        if (referenceBean.getMilliSeconds() <= 0
                || (RpcConstants.DEFAULT_RATELIMITER_MILLI_SECONDS == referenceBean.getMilliSeconds() && springBootConsumerConfig.getMilliSeconds() > 0)){
            referenceBean.setMilliSeconds(springBootConsumerConfig.getMilliSeconds());
        }

        if (StringUtils.isEmpty(referenceBean.getRateLimiterFailStrategy())
                || (RpcConstants.RATE_LIMILTER_FAIL_STRATEGY_DIRECT.equals(referenceBean.getRateLimiterFailStrategy()) && !StringUtils.isEmpty(springBootConsumerConfig.getRateLimiterFailStrategy()))){
            referenceBean.setRateLimiterFailStrategy(springBootConsumerConfig.getRateLimiterFailStrategy());
        }

        if (!referenceBean.isEnableFusing()){
            referenceBean.setEnableFusing(springBootConsumerConfig.getEnableFusing());
        }

        if (StringUtils.isEmpty(referenceBean.getFusingType())
                || (RpcConstants.DEFAULT_FUSING_INVOKER.equals(referenceBean.getFusingType()) && !StringUtils.isEmpty(springBootConsumerConfig.getFusingType()))){
            referenceBean.setFusingType(springBootConsumerConfig.getFusingType());
        }

        if (referenceBean.getTotalFailure() <= 0
                || (RpcConstants.DEFAULT_FUSING_TOTAL_FAILURE == referenceBean.getTotalFailure() && springBootConsumerConfig.getTotalFailure() > 0 )){
            referenceBean.setTotalFailure(springBootConsumerConfig.getTotalFailure());
        }

        if (referenceBean.getFusingMilliSeconds() <= 0
                || (RpcConstants.DEFAULT_FUSING_MILLI_SECONDS == referenceBean.getFusingMilliSeconds() && springBootConsumerConfig.getFusingMilliSeconds() > 0)){
            referenceBean.setFusingMilliSeconds(springBootConsumerConfig.getFusingMilliSeconds());
        }

        if (StringUtils.isEmpty(referenceBean.getExceptionPostProcessorType())
                || (RpcConstants.EXCEPTION_POST_PROCESSOR_PRINT.equals(referenceBean.getExceptionPostProcessorType()) && !StringUtils.isEmpty(springBootConsumerConfig.getExceptionPostProcessorType()))){
            referenceBean.setExceptionPostProcessorType(springBootConsumerConfig.getExceptionPostProcessorType());
        }
        return referenceBean;
    }
}
