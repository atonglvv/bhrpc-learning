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
package io.binghe.rpc.proxy.api.object;

import io.binghe.rpc.cache.result.CacheResultKey;
import io.binghe.rpc.cache.result.CacheResultManager;
import io.binghe.rpc.common.exception.RpcException;
import io.binghe.rpc.common.utils.StringUtils;
import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.fusing.api.FusingInvoker;
import io.binghe.rpc.protocol.RpcProtocol;
import io.binghe.rpc.protocol.enumeration.RpcType;
import io.binghe.rpc.protocol.header.RpcHeaderFactory;
import io.binghe.rpc.protocol.request.RpcRequest;
import io.binghe.rpc.proxy.api.async.IAsyncObjectProxy;
import io.binghe.rpc.proxy.api.consumer.Consumer;
import io.binghe.rpc.proxy.api.future.RPCFuture;
import io.binghe.rpc.ratelimiter.api.RateLimiterInvoker;
import io.binghe.rpc.reflect.api.ReflectInvoker;
import io.binghe.rpc.registry.api.RegistryService;
import io.binghe.rpc.spi.loader.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 对象代理类
 */
public class ObjectProxy <T> implements IAsyncObjectProxy, InvocationHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectProxy.class);

    /**
     * 接口的Class对象
     */
    private Class<T> clazz;

    /**
     * 服务版本号
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 超时时间，默认15s
     */
    private long timeout = 15000;

    /**
     * 注册服务
     */
    private RegistryService registryService;

    /**
     * 服务消费者
     */
    private Consumer consumer;
    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    /**
     * 是否开启结果缓存
     */
    private boolean enableResultCache;

    /**
     * 结果缓存管理器
     */
    private CacheResultManager<Object> cacheResultManager;

    /**
     * 反射调用方法
     */
    private ReflectInvoker reflectInvoker;

    /**
     * 容错Class类
     */
    private Class<?> fallbackClass;

    /**
     * 限流规则SPI接口
     */
    private RateLimiterInvoker rateLimiterInvoker;

    /**
     * 是否开启限流
     */
    private boolean enableRateLimiter;

    /**
     * 当限流失败时的处理策略
     */
    private String rateLimiterFailStrategy;

    /**
     * 是否开启熔断
     */
    private boolean enableFusing;

    /**
     * 熔断SPI接口
     */
    private FusingInvoker fusingInvoker;

    public ObjectProxy(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ObjectProxy(Class<T> clazz, String serviceVersion, String serviceGroup, String serializationType, long timeout,
                       RegistryService registryService, Consumer consumer, boolean async, boolean oneway, boolean enableResultCache,
                       int resultCacheExpire, String reflectType, String fallbackClassName, Class<?> fallbackClass,
                       boolean enableRateLimiter, String rateLimiterType, int permits, int milliSeconds, String rateLimiterFailStrategy,
                       boolean enableFusing, String fusingType, double totalFailure, int fusingMilliSeconds) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.timeout = timeout;
        this.serviceGroup = serviceGroup;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = registryService;
        this.enableResultCache = enableResultCache;
        if (resultCacheExpire <= 0){
            resultCacheExpire = RpcConstants.RPC_SCAN_RESULT_CACHE_EXPIRE;
        }
        this.cacheResultManager = CacheResultManager.getInstance(resultCacheExpire, enableResultCache);
        this.reflectInvoker = ExtensionLoader.getExtension(ReflectInvoker.class, reflectType);
        this.fallbackClass = this.getFallbackClass(fallbackClassName, fallbackClass);
        this.enableRateLimiter = enableRateLimiter;
        this.initRateLimiter(rateLimiterType, permits, milliSeconds);
        if (StringUtils.isEmpty(rateLimiterFailStrategy)){
            rateLimiterFailStrategy = RpcConstants.RATE_LIMILTER_FAIL_STRATEGY_DIRECT;
        }
        this.rateLimiterFailStrategy = rateLimiterFailStrategy;
        this.enableFusing = enableFusing;
        this.initFusing(fusingType, totalFailure, fusingMilliSeconds);
    }

    /**
     * 初始化熔断SPI接口
     */
    private void initFusing(String fusingType, double totalFailure, int fusingMilliSeconds) {
        if (enableFusing){
            fusingType = StringUtils.isEmpty(fusingType) ? RpcConstants.DEFAULT_FUSING_INVOKER : fusingType;
            this.fusingInvoker = ExtensionLoader.getExtension(FusingInvoker.class, fusingType);
            this.fusingInvoker.init(totalFailure, fusingMilliSeconds);
        }
    }

    /**
     * 初始化限流器
     */
    private void initRateLimiter(String rateLimiterType, int permits, int milliSeconds) {
        if (enableRateLimiter){
            rateLimiterType = StringUtils.isEmpty(rateLimiterType) ? RpcConstants.DEFAULT_RATELIMITER_INVOKER : rateLimiterType;
            this.rateLimiterInvoker = ExtensionLoader.getExtension(RateLimiterInvoker.class, rateLimiterType);
            this.rateLimiterInvoker.init(permits, milliSeconds);
        }
    }

    /**
     * 优先使用fallbackClass，如果fallbackClass为空，则使用fallbackClassName
     */
    private Class<?> getFallbackClass(String fallbackClassName, Class<?> fallbackClass) {
        if (this.isFallbackClassEmpty(fallbackClass)){
            try {
                if (!StringUtils.isEmpty(fallbackClassName)){
                    fallbackClass = Class.forName(fallbackClassName);
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
            }
        }
        return fallbackClass;
    }

    /**
     * 容错class为空
     */
    private boolean isFallbackClassEmpty(Class<?> fallbackClass){
        return fallbackClass == null
                || fallbackClass == RpcConstants.DEFAULT_FALLBACK_CLASS
                || RpcConstants.DEFAULT_FALLBACK_CLASS.equals(fallbackClass);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" +
                        Integer.toHexString(System.identityHashCode(proxy)) +
                        ", with InvocationHandler " + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }
        //开启缓存，直接调用方法请求服务提供者
        if (enableResultCache) return invokeSendRequestMethodCache(method, args);
        return invokeSendRequestMethodWithFallback(method, args);
    }

    private Object invokeSendRequestMethodCache(Method method, Object[] args) throws Exception {
        //开启缓存，则处理缓存
        CacheResultKey cacheResultKey = new CacheResultKey(method.getDeclaringClass().getName(), method.getName(), method.getParameterTypes(), args, serviceVersion, serviceGroup);
        Object obj = this.cacheResultManager.get(cacheResultKey);
        if (obj == null){
            obj = invokeSendRequestMethodWithFallback(method, args);
            if (obj != null){
                cacheResultKey.setCacheTimeStamp(System.currentTimeMillis());
                this.cacheResultManager.put(cacheResultKey, obj);
            }
        }
        return obj;
    }

    /**
     * 以容错方式真正发送请求
     */
    private Object invokeSendRequestMethodWithFallback(Method method, Object[] args) throws Exception {
        try {
            return invokeSendRequestMethodWithRateLimiter(method, args);
        }catch (Throwable e){
            return getFallbackResult(method, args);
        }
    }

    /**
     * 以限流方式发送请求
     */
    private Object invokeSendRequestMethodWithRateLimiter(Method method, Object[] args) throws Exception{
        Object result = null;
        if (enableRateLimiter){
            if (rateLimiterInvoker.tryAcquire()){
                try {
                    result = invokeSendRequestMethodWithFsing(method, args);
                }finally {
                    rateLimiterInvoker.release();
                }
            }else {
                result = this.invokeFailRateLimiterMethod(method, args);
            }
        }else {
            result = invokeSendRequestMethodWithFsing(method, args);
        }
        return result;
    }

    /**
     * 执行限流失败时的处理逻辑
     */
    private Object invokeFailRateLimiterMethod(Method method, Object[] args) throws Exception{
        LOGGER.info("execute {} fail rate limiter strategy...", rateLimiterFailStrategy);
        switch (rateLimiterFailStrategy){
            case RpcConstants.RATE_LIMILTER_FAIL_STRATEGY_EXCEPTION:
            case RpcConstants.RATE_LIMILTER_FAIL_STRATEGY_FALLBACK:
                return this.getFallbackResult(method, args);
            case RpcConstants.RATE_LIMILTER_FAIL_STRATEGY_DIRECT:
                return this.invokeSendRequestMethodWithFsing(method, args);
        }
        return this.invokeSendRequestMethodWithFsing(method, args);
    }


    /**
     * 以熔断方式请求数据
     */
    private Object invokeSendRequestMethodWithFsing(Method method, Object[] args) throws Exception{
        //开启了熔断
        if (enableFusing){
            return invokeFusingSendRequestMethod(method, args);
        }else {
            return invokeSendRequestMethod(method, args);
        }
    }

    /**
     * 熔断请求
     */
    private Object invokeFusingSendRequestMethod(Method method, Object[] args) throws Exception {
        //触发了熔断规则，直接返回降级处理业务
        if (fusingInvoker.invokeFusingStrategy()){
            return this.getFallbackResult(method, args);
        }
        //请求计数器加1
        fusingInvoker.incrementCount();
        Object result = null;
        try {
            result = invokeSendRequestMethod(method, args);
            if (fusingInvoker.isHalfOpenStatus()){
                fusingInvoker.compareAndSetWaitStatus(RpcConstants.FUSING_WAIT_STATUS_WAITINF, RpcConstants.FUSING_WAIT_STATUS_SUCCESS);
            }
        }catch (Throwable e){
            fusingInvoker.incrementFailureCount();
            if (fusingInvoker.isHalfOpenStatus()){
                fusingInvoker.compareAndSetWaitStatus(RpcConstants.FUSING_WAIT_STATUS_WAITINF, RpcConstants.FUSING_WAIT_STATUS_FAILED);
            }
            throw new RpcException(e.getMessage());
        }
        return result;
    }


    /**
     * 真正发送请求调用远程方法
     */
    private Object invokeSendRequestMethod(Method method, Object[] args) throws Exception {
        RpcProtocol<RpcRequest> requestRpcProtocol = getSendRequest(method, args);
        RPCFuture rpcFuture = this.consumer.sendRequest(requestRpcProtocol, registryService);
        return rpcFuture == null ? null : timeout > 0 ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS) : rpcFuture.get();
    }

    /**
     * 获取容错结果
     */
    private Object getFallbackResult(Method method, Object[] args) {
        try {
            //fallbackClass不为空，则执行容错处理
            if (this.isFallbackClassEmpty(fallbackClass)){
                return null;
            }
            return reflectInvoker.invokeMethod(fallbackClass.newInstance(), fallbackClass, method.getName(), method.getParameterTypes(), args);
        } catch (Throwable ex) {
            LOGGER.error(ex.getMessage());
        }
        return null;
    }

    /**
     * 封装请求协议对象
     */
    private RpcProtocol<RpcRequest> getSendRequest(Method method, Object[] args) {
        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<RpcRequest>();

        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType, RpcType.REQUEST.getType()));

        RpcRequest request = new RpcRequest();
        request.setVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setGroup(this.serviceGroup);
        request.setParameters(args);
        request.setAsync(async);
        request.setOneway(oneway);
        requestRpcProtocol.setBody(request);

        // Debug
        LOGGER.debug(method.getDeclaringClass().getName());
        LOGGER.debug(method.getName());

        if (method.getParameterTypes() != null && method.getParameterTypes().length > 0){
            for (int i = 0; i < method.getParameterTypes().length; ++i) {
                LOGGER.debug(method.getParameterTypes()[i].getName());
            }
        }

        if (args != null && args.length > 0){
            for (int i = 0; i < args.length; ++i) {
                LOGGER.debug(args[i].toString());
            }
        }
        return requestRpcProtocol;
    }

    @Override
    public RPCFuture call(String funcName, Object... args) {
        RpcProtocol<RpcRequest> request = getCallRequest(this.clazz.getName(), funcName, args);
        RPCFuture rpcFuture = null;
        try {
            rpcFuture = this.consumer.sendRequest(request, registryService);
        } catch (Exception e) {
            LOGGER.error("async all throws exception:{}", e);
        }
        return rpcFuture;
    }

    private RpcProtocol<RpcRequest> getCallRequest(String className, String methodName, Object[] args) {

        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<RpcRequest>();

        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType, RpcType.REQUEST.getType()));

        RpcRequest request = new RpcRequest();
        request.setClassName(className);
        request.setMethodName(methodName);
        request.setParameters(args);
        request.setVersion(this.serviceVersion);
        request.setGroup(this.serviceGroup);

        Class[] parameterTypes = new Class[args.length];
        // Get the right class type
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }
        request.setParameterTypes(parameterTypes);
        requestRpcProtocol.setBody(request);

        LOGGER.debug(className);
        LOGGER.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            LOGGER.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < args.length; ++i) {
            LOGGER.debug(args[i].toString());
        }

        return requestRpcProtocol;
    }

    private Class<?> getClassType(Object obj){
        Class<?> classType = obj.getClass();
        String typeName = classType.getName();
        switch (typeName){
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }
        return classType;
    }
}
