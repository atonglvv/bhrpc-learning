/**
 * Copyright 2022-9999 the original author or authors.
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
package io.binghe.rpc.cache.result;

import io.binghe.rpc.constants.RpcConstants;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 结果缓存管理器
 */
public class CacheResultManager<T> {

    /**
     * 缓存结果信息
     */
    private final Map<CacheResultKey, T> cacheResult = new ConcurrentHashMap<>(4096);

    /**
     * 扫描结果缓存的线程池
     */
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    /**
     * 读写锁
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * 读锁
     */
    private final Lock readLock = lock.readLock();

    /**
     * 写锁
     */
    private final Lock writeLock = lock.writeLock();

    /**
     * 结果缓存过期时长，单位毫秒
     */
    private int resultCacheExpire;

    public CacheResultManager(int resultCacheExpire, boolean enableResultCache){
        this.resultCacheExpire = resultCacheExpire;
        if (enableResultCache){
            this.startScanTask();
        }
    }

    /**
     * 扫描结果缓存
     */
    private void startScanTask() {
        scheduledExecutorService.scheduleAtFixedRate(()->{
            if (cacheResult.size() > 0){
                writeLock.lock();
                try{
                    Iterator<Map.Entry<CacheResultKey, T>> iterator = cacheResult.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry<CacheResultKey, T> entry = iterator.next();
                        CacheResultKey cacheKey = entry.getKey();
                        //当时间减去保存数据的缓存时间大于配置的时间间隔，则需要剔除缓存数据
                        if (System.currentTimeMillis() - cacheKey.getCacheTimeStamp() > resultCacheExpire){
                            cacheResult.remove(cacheKey);
                        }
                    }
                }finally {
                    writeLock.unlock();
                }
            }
        }, 0, RpcConstants.RPC_SCAN_RESULT_CACHE_TIME_INTERVAL, TimeUnit.MILLISECONDS);
    }


    /**
     * 获取缓存中的数据
     */
    public T get(CacheResultKey key){
        return cacheResult.get(key);
    }

    /**
     * 缓存数据
     */
    public void put(CacheResultKey key, T value){
        writeLock.lock();
        try {
            cacheResult.put(key, value);
        }finally {
            writeLock.unlock();
        }
    }
}
