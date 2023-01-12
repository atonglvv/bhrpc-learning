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
package io.binghe.rpc.loadbalancer.helper;

import io.binghe.rpc.common.utils.CollectionUtils;
import io.binghe.rpc.protocol.meta.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 服务负载均衡辅助类
 */
public class ServiceLoadBalancerHelper {

    /**
     * 临时记录ServiceInstance<ServiceMeta>列表
     */
    private static volatile List<ServiceInstance<ServiceMeta>> tempServiceInstances = new ArrayList<>();

    /**
     * 缓存List<ServiceMeta>
     */
    private static volatile Map<String, List<ServiceMeta>> serviceMetaMap = new ConcurrentHashMap<>();

    /**
     * 缓存的Key
     */
    private static final String CACHE_KEY = "cache_key";

    /**
     * 通过List<ServiceInstance<ServiceMeta>>列表获取List<ServiceMeta>
     */
    public static List<ServiceMeta> getServiceMetaList(List<ServiceInstance<ServiceMeta>> serviceInstances){
        List<ServiceMeta> resultList = null;
        if (CollectionUtils.isEmpty(serviceInstances)) return resultList;
        //元数据列表有变动
        if (!CollectionUtils.equals(tempServiceInstances, serviceInstances)){
            resultList = getServiceMetaListFromChange(serviceInstances);
        }else{
            resultList = getServiceMetaListFromCache(serviceInstances);
        }
        return resultList;
    }

    /**
     * 缓存列表变动
     */
    private static List<ServiceMeta> getServiceMetaListFromChange(List<ServiceInstance<ServiceMeta>> serviceInstances) {
        tempServiceInstances = serviceInstances;
        List<ServiceMeta> resultList = getServiceMetaListFromInstance(serviceInstances);
        serviceMetaMap.put(CACHE_KEY, resultList);
        return resultList;
    }

    /**
     * 从缓存中获取
     */
    private static List<ServiceMeta> getServiceMetaListFromCache(List<ServiceInstance<ServiceMeta>> serviceInstances) {
        List<ServiceMeta> serviceMetaList = serviceMetaMap.get(CACHE_KEY);
        if (CollectionUtils.isEmpty(serviceMetaList)){
            serviceMetaList = getServiceMetaListFromInstance(serviceInstances);
            serviceMetaMap.put(CACHE_KEY, serviceMetaList);
        }
        return serviceMetaList;
    }

    /**
     * 数据转换
     */
    private static List<ServiceMeta> getServiceMetaListFromInstance(List<ServiceInstance<ServiceMeta>> serviceInstances) {
        List<ServiceMeta> list = new ArrayList<>(serviceInstances.size());
        for (ServiceInstance<ServiceMeta> serviceInstance : serviceInstances){
            list.add(serviceInstance.getPayload());
        }
        return list;
    }
}
