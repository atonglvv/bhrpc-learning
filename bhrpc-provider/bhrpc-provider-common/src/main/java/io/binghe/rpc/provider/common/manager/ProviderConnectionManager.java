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
package io.binghe.rpc.provider.common.manager;

import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.protocol.RpcProtocol;
import io.binghe.rpc.protocol.enumeration.RpcType;
import io.binghe.rpc.protocol.header.RpcHeader;
import io.binghe.rpc.protocol.header.RpcHeaderFactory;
import io.binghe.rpc.protocol.response.RpcResponse;
import io.binghe.rpc.provider.common.cache.ProviderChannelCache;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 服务提供者连接管理器
 */
public class ProviderConnectionManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProviderConnectionManager.class);
    /**
     * 扫描并移除不活跃的连接
     */
    public static void scanNotActiveChannel(){
        Set<Channel> channelCache = ProviderChannelCache.getChannelCache();
        if (channelCache == null || channelCache.isEmpty()) return;
        channelCache.stream().forEach((channel) -> {
            if (!channel.isOpen() || !channel.isActive()){
                channel.close();
                ProviderChannelCache.remove(channel);
            }
        });
    }

    /**
     * 发送ping消息
     */
    public static void broadcastPingMessageFromProvider(){
        Set<Channel> channelCache = ProviderChannelCache.getChannelCache();
        if (channelCache == null || channelCache.isEmpty()) return;
        RpcHeader header = RpcHeaderFactory.getRequestHeader(RpcConstants.SERIALIZATION_PROTOSTUFF, RpcType.HEARTBEAT_FROM_PROVIDER.getType());
        RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<RpcResponse>();
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setResult(RpcConstants.HEARTBEAT_PING);
        responseRpcProtocol.setHeader(header);
        responseRpcProtocol.setBody(rpcResponse);
        channelCache.stream().forEach((channel) -> {
            if (channel.isOpen() && channel.isActive()){
                LOGGER.info("send heartbeat message to service consumer, the consumer is: {}, the heartbeat message is: {}", channel.remoteAddress(), rpcResponse.getResult());
                channel.writeAndFlush(responseRpcProtocol);
            }
        });
    }
}
