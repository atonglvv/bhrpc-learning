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
package io.binghe.rpc.ratelimiter.guava;

import com.google.common.util.concurrent.RateLimiter;
import io.binghe.rpc.ratelimiter.base.AbstractRateLimiterInvoker;
import io.binghe.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 基于Guava的限流策略
 */
@SPIClass
public class GuavaRateLimiterInvoker extends AbstractRateLimiterInvoker {
    private final Logger logger = LoggerFactory.getLogger(GuavaRateLimiterInvoker.class);
    private RateLimiter rateLimiter;

    @Override
    public boolean tryAcquire() {
        logger.info("execute guava rate limiter...");
        return this.rateLimiter.tryAcquire();
    }

    @Override
    public void release() {
        //TODO ignore
    }

    @Override
    public void init(int permits, int milliSeconds) {
        super.init(permits, milliSeconds);
        //转换成每秒钟最多允许的个数
        double permitsPerSecond = ((double) permits) / milliSeconds * 1000;
        this.rateLimiter = RateLimiter.create(permitsPerSecond);
    }
}
