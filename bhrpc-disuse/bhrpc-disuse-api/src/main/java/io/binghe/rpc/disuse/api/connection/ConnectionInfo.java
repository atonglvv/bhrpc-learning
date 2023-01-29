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
package io.binghe.rpc.disuse.api.connection;

import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 连接信息
 */
public class ConnectionInfo implements Serializable {
    private static final long serialVersionUID = -9165095996736033806L;

    /**
     * Channel连接
     */
    private Channel channel;

    /**
     * 连接的时间
     */
    private long connectionTime;

    /**
     * 最后使用时间
     */
    private long lastUseTime;

    /**
     * 使用次数
     */
    private AtomicInteger useCount = new AtomicInteger(0);

    public ConnectionInfo() {
    }

    public ConnectionInfo(Channel channel) {
        this.channel = channel;
        long currentTimeStamp = System.currentTimeMillis();
        this.connectionTime = currentTimeStamp;
        this.lastUseTime = currentTimeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionInfo info = (ConnectionInfo) o;
        return Objects.equals(channel, info.channel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel);
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getConnectionTime() {
        return connectionTime;
    }

    public void setConnectionTime(long connectionTime) {
        this.connectionTime = connectionTime;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public int getUseCount() {
        return useCount.get();
    }

    public int incrementUseCount() {
        return this.useCount.incrementAndGet();
    }
}
