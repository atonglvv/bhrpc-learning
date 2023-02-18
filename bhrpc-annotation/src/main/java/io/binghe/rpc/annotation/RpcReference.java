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
package io.binghe.rpc.annotation;

import io.binghe.rpc.constants.RpcConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author binghe
 * @version 1.0.0
 * @description bhrpc服务消费者，配置优先级：服务消费者字段上配置的@RpcReference注解属性 > yml文件 > @RpcReference默认注解属性
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {

    /**
     * 版本号
     */
    String version() default RpcConstants.RPC_COMMON_DEFAULT_VERSION;

    /**
     * 注册中心类型, 目前的类型包含：zookeeper、nacos、etcd、consul
     */
    String registryType() default RpcConstants.RPC_REFERENCE_DEFAULT_REGISTRYTYPE;

    /**
     * 注册地址
     */
    String registryAddress() default RpcConstants.RPC_REFERENCE_DEFAULT_REGISTRYADDRESS;

    /**
     * 负载均衡类型，默认基于ZK的一致性Hash
     */
    String loadBalanceType() default RpcConstants.RPC_REFERENCE_DEFAULT_LOADBALANCETYPE;

    /**
     * 序列化类型，目前的类型包含：protostuff、kryo、json、jdk、hessian2、fst
     */
    String serializationType() default RpcConstants.RPC_REFERENCE_DEFAULT_SERIALIZATIONTYPE;

    /**
     * 超时时间，默认5s
     */
    long timeout() default RpcConstants.RPC_REFERENCE_DEFAULT_TIMEOUT;

    /**
     * 是否异步执行
     */
    boolean async() default false;

    /**
     * 是否单向调用
     */
    boolean oneway() default false;

    /**
     * 代理的类型，jdk：jdk代理， javassist: javassist代理, cglib: cglib代理
     */
    String proxy() default RpcConstants.RPC_REFERENCE_DEFAULT_PROXY;

    /**
     * 服务分组，默认为空
     */
    String group() default RpcConstants.RPC_COMMON_DEFAULT_GROUP;

    /**
     * 心跳间隔时间，默认30秒
     */
    int heartbeatInterval() default RpcConstants.RPC_COMMON_DEFAULT_HEARTBEATINTERVAL;

    /**
     * 扫描空闲连接间隔时间，默认60秒
     */
    int scanNotActiveChannelInterval() default RpcConstants.RPC_COMMON_DEFAULT_SCANNOTACTIVECHANNELINTERVAL;

    /**
     * 重试间隔时间
     */
    int retryInterval() default RpcConstants.RPC_REFERENCE_DEFAULT_RETRYINTERVAL;

    /**
     * 重试间隔时间
     */
    int retryTimes() default RpcConstants.RPC_REFERENCE_DEFAULT_RETRYTIMES;

    /**
     * 是否开启结果缓存
     */
    boolean enableResultCache() default false;

    /**
     * 缓存结果的时长，单位是毫秒
     */
    int resultCacheExpire() default RpcConstants.RPC_SCAN_RESULT_CACHE_EXPIRE;


    /**
     * 是否开启直连服务
     */
    boolean enableDirectServer() default false;

    /**
     * 直连服务的地址
     */
    String directServerUrl() default RpcConstants.RPC_COMMON_DEFAULT_DIRECT_SERVER;

    /**
     * 是否开启延迟连接
     */
    boolean enableDelayConnection() default false;

    /**
     * 默认并发线程池核心线程数
     */
    int corePoolSize() default RpcConstants.DEFAULT_CORE_POOL_SIZE;

    /**
     * 默认并发线程池最大线程数
     */
    int maximumPoolSize() default RpcConstants.DEFAULT_MAXI_NUM_POOL_SIZE;


    /**
     * 流控分析类型
     */
    String flowType() default RpcConstants.FLOW_POST_PROCESSOR_PRINT;


    /**
     * 是否开启缓冲区
     */
    boolean enableBuffer() default false;

    /**
     * 缓冲区大小
     */
    int bufferSize() default RpcConstants.DEFAULT_BUFFER_SIZE;


    /**
     * 容错class
     */
    Class<?> fallbackClass() default void.class;

    /**
     * 容错class名称
     */
    String fallbackClassName() default RpcConstants.DEFAULT_FALLBACK_CLASS_NAME;

    /**
     * 反射类型
     */
    String reflectType() default RpcConstants.DEFAULT_REFLECT_TYPE;

    /**
     * 是否开启限流
     */
    boolean enableRateLimiter() default false;

    /**
     * 限流类型
     */
    String rateLimiterType() default RpcConstants.DEFAULT_RATELIMITER_INVOKER;

    /**
     * 在milliSeconds毫秒内最多能够通过的请求个数
     */
    int permits() default RpcConstants.DEFAULT_RATELIMITER_PERMITS;

    /**
     * 毫秒数
     */
    int milliSeconds() default RpcConstants.DEFAULT_RATELIMITER_MILLI_SECONDS;

    /**
     * 当限流失败时的处理策略
     */
    String rateLimiterFailStrategy() default RpcConstants.RATE_LIMILTER_FAIL_STRATEGY_DIRECT;

    /**
     * 是否开启熔断策略
     */
    boolean enableFusing() default false;
    /**
     * 熔断规则标识
     */
    String fusingType() default RpcConstants.DEFAULT_FUSING_INVOKER;

    /**
     * 在fusingMilliSeconds毫秒内触发熔断操作的上限值
     */
    double totalFailure() default RpcConstants.DEFAULT_FUSING_TOTAL_FAILURE;

    /**
     * 熔断的毫秒时长
     */
    int fusingMilliSeconds() default RpcConstants.DEFAULT_FUSING_MILLI_SECONDS;


    /**
     * 异常处理类型
     */
    String exceptionPostProcessorType() default RpcConstants.EXCEPTION_POST_PROCESSOR_PRINT;
}
