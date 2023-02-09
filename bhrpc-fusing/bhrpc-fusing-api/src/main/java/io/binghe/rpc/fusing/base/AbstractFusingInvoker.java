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
package io.binghe.rpc.fusing.base;

import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.fusing.api.FusingInvoker;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 抽象熔断类
 */
public abstract class AbstractFusingInvoker implements FusingInvoker {

    /**
     * 熔断状态，1：关闭； 2：半开启； 3：开启
     */
    protected final AtomicInteger fusingStatus = new AtomicInteger(RpcConstants.FUSING_STATUS_CLOSED);

    /**
     * 当前调用次数
     */
    protected final AtomicInteger currentCounter = new AtomicInteger(0);

    /**
     * 当前调用失败的次数
     */
    protected final AtomicInteger currentFailureCounter = new AtomicInteger(0);

    /**
     * 熔断时间范围的开始时间点
     */
    protected volatile long lastTimeStamp = System.currentTimeMillis();

    /**
     * 在milliSeconds毫秒内触发熔断操作的上限值
     * 可能是错误个数，也可能是错误率
     */
    protected double totalFailure;

    /**
     * 毫秒数
     */
    protected int milliSeconds;

    /**
     * 重置数量
     */
    protected void resetCount(){
        currentFailureCounter.set(0);
        currentCounter.set(0);
    }

    @Override
    public void incrementCount() {
        currentCounter.incrementAndGet();
    }

    @Override
    public void incrementFailureCount() {
        currentFailureCounter.incrementAndGet();
    }

    @Override
    public void init(double totalFailure, int milliSeconds) {
        this.totalFailure = totalFailure <= 0 ? RpcConstants.DEFAULT_FUSING_TOTAL_FAILURE : totalFailure;
        this.milliSeconds = milliSeconds <= 0 ? RpcConstants.DEFAULT_FUSING_MILLI_SECONDS : milliSeconds;
    }
}
