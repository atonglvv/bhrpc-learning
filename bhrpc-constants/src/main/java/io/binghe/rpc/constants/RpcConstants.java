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
package io.binghe.rpc.constants;

/**
 * @author binghe
 * @version 1.0.0
 * @description 常量类
 */
public class RpcConstants {

    /**
     * 消息头，固定32个字节
     */
    public static final int HEADER_TOTAL_LEN = 32;

    /**
     * 魔数
     */
    public static final short MAGIC = 0x10;

    /**
     * 版本号
     */
    public static final byte VERSION = 0x1;

    /**
     * REFLECT_TYPE_JDK
     */
    public static final String REFLECT_TYPE_JDK = "jdk";

    /**
     * REFLECT_TYPE_CGLIB
     */
    public static final String REFLECT_TYPE_CGLIB = "cglib";

    /**
     * JDK动态代理
     */
    public static final String PROXY_JDK = "jdk";
    /**
     * javassist动态代理
     */
    public static final String PROXY_JAVASSIST = "javassist";
    /**
     * cglib动态代理
     */
    public static final String PROXY_CGLIB = "cglib";

    /**
     * 初始化的方法
     */
    public static final String INIT_METHOD_NAME = "init";

    /**
     * zookeeper
     */
    public static final String REGISTRY_CENTER_ZOOKEEPER = "zookeeper";
    /**
     * nacos
     */
    public static final String REGISTRY_CENTER_NACOS = "nacos";
    /**
     * apoll
     */
    public static final String REGISTRY_CENTER_APOLL = "apoll";
    /**
     * etcd
     */
    public static final String REGISTRY_CENTER_ETCD = "etcd";
    /**
     * eureka
     */
    public static final String REGISTRY_CENTER_EUREKA = "eureka";

    /**
     * protostuff 序列化
     */
    public static final String SERIALIZATION_PROTOSTUFF = "protostuff";
    /**
     * FST 序列化
     */
    public static final String SERIALIZATION_FST = "fst";
    /**
     * hessian2 序列化
     */
    public static final String SERIALIZATION_HESSIAN2 = "hessian2";
    /**
     * jdk 序列化
     */
    public static final String SERIALIZATION_JDK = "jdk";
    /**
     * json 序列化
     */
    public static final String SERIALIZATION_JSON = "json";
    /**
     * kryo 序列化
     */
    public static final String SERIALIZATION_KRYO = "kryo";
    /**
     * 基于ZK的一致性Hash负载均衡
     */
    public static final String SERVICE_LOAD_BALANCER_ZKCONSISTENTHASH = "zkconsistenthash";
    /**
     * 基于随机算法的负载均衡
     */
    public static final String SERVICE_LOAD_BALANCER_RANDOM = "random";

    /**
     * 增强型负载均衡前缀
     */
    public static final String SERVICE_ENHANCED_LOAD_BALANCER_PREFIX = "enhanced_";

    /**
     * 最小权重
     */
    public static final int SERVICE_WEIGHT_MIN = 1;
    /**
     * 最大权重
     */
    public static final int SERVICE_WEIGHT_MAX = 100;

    /**
     * 心跳ping消息
     */
    public static final String HEARTBEAT_PING = "ping";

    /**
     * 心跳pong消息
     */
    public static final String HEARTBEAT_PONG = "pong";

}
