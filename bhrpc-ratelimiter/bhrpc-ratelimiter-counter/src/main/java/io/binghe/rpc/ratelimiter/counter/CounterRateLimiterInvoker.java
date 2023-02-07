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
package io.binghe.rpc.ratelimiter.counter;

import io.binghe.rpc.ratelimiter.base.AbstractRateLimiterInvoker;
import io.binghe.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 计数器限流
 */
@SPIClass
public class CounterRateLimiterInvoker extends AbstractRateLimiterInvoker {

    private final Logger logger = LoggerFactory.getLogger(CounterRateLimiterInvoker.class);
    private final AtomicInteger currentCounter = new AtomicInteger(0);
    private volatile long lastTimeStamp = System.currentTimeMillis();

    @Override
    public boolean tryAcquire() {
        logger.info("execute counter rate limiter...");
        //获取当前时间
        long currentTimeStamp = System.currentTimeMillis();
        //超过一个执行周期
        if (currentTimeStamp - lastTimeStamp >= milliSeconds){
            lastTimeStamp = currentTimeStamp;
            currentCounter.set(0);
            return true;
        }
        //当前请求数小于配置的数量
        if (currentCounter.incrementAndGet() <= permits){
            return true;
        }
        return false;
    }

    @Override
    public void release() {
        //TODO ignore
    }
}
