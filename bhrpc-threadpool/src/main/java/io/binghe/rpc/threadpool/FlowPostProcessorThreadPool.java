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
package io.binghe.rpc.threadpool;

import io.binghe.rpc.constants.RpcConstants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 流量后置处理器线程池
 */
public class FlowPostProcessorThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor;
    static {
        threadPoolExecutor = new ThreadPoolExecutor(8, 8, RpcConstants.DEFAULT_KEEP_ALIVE_TIME, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(RpcConstants.DEFAULT_QUEUE_CAPACITY));
    }

    public static void submit(Runnable task){
        threadPoolExecutor.submit(task);
    }

    public static void shutdown(){
        threadPoolExecutor.shutdown();
    }
}
