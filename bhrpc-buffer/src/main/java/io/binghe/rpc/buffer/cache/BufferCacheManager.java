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
package io.binghe.rpc.buffer.cache;

import io.binghe.rpc.common.exception.RpcException;
import io.binghe.rpc.constants.RpcConstants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 缓冲区实现
 */
public class BufferCacheManager<T> {
    //缓冲队列
    private BlockingQueue<T> bufferQueue;
    //缓存管理器单例对象
    private static volatile BufferCacheManager instance;

    //私有构造方法
    private BufferCacheManager(int bufferSize){
        if (bufferSize <= 0){
            bufferSize = RpcConstants.DEFAULT_BUFFER_SIZE;
        }
        this.bufferQueue = new ArrayBlockingQueue<>(bufferSize);
    }

    //创建单例对象
    public static <T> BufferCacheManager<T> getInstance(int bufferSize){
        if (instance == null){
            synchronized (BufferCacheManager.class){
                if (instance == null){
                    instance = new BufferCacheManager(bufferSize);
                }
            }
        }
        return instance;
    }

    //向缓冲区添加元素
    public void put(T t){
        try {
            bufferQueue.put(t);
        } catch (InterruptedException e) {
            throw new RpcException(e);
        }
    }

    //获取缓冲区元素
    public T take(){
        try {
            return bufferQueue.take();
        } catch (InterruptedException e) {
            throw new RpcException(e);
        }
    }
}
