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
package io.binghe.rpc.consumer.common.initializer;

import io.binghe.rpc.codec.RpcDecoder;
import io.binghe.rpc.codec.RpcEncoder;
import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.consumer.common.handler.RpcConsumerHandler;
import io.binghe.rpc.flow.processor.FlowPostProcessor;
import io.binghe.rpc.threadpool.ConcurrentThreadPool;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description RpcConsumerInitializer
 */
public class RpcConsumerInitializer extends ChannelInitializer<SocketChannel> {
    private int heartbeatInterval;
    private ConcurrentThreadPool concurrentThreadPool;
    private FlowPostProcessor flowPostProcessor;
    public RpcConsumerInitializer(int heartbeatInterval, ConcurrentThreadPool concurrentThreadPool, FlowPostProcessor flowPostProcessor){
        if (heartbeatInterval > 0){
            this.heartbeatInterval = heartbeatInterval;
        }
        this.concurrentThreadPool = concurrentThreadPool;
        this.flowPostProcessor = flowPostProcessor;
    }
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline cp = channel.pipeline();
        cp.addLast(RpcConstants.CODEC_ENCODER, new RpcEncoder(flowPostProcessor));
        cp.addLast(RpcConstants.CODEC_DECODER, new RpcDecoder(flowPostProcessor));
        cp.addLast(RpcConstants.CODEC_CLIENT_IDLE_HANDLER, new IdleStateHandler(heartbeatInterval, 0, 0, TimeUnit.MILLISECONDS));
        cp.addLast(RpcConstants.CODEC_HANDLER, new RpcConsumerHandler(concurrentThreadPool));
    }
}
