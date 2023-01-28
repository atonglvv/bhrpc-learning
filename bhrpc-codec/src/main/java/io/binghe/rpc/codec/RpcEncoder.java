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
package io.binghe.rpc.codec;

import io.binghe.rpc.common.utils.SerializationUtils;
import io.binghe.rpc.flow.processor.FlowPostProcessor;
import io.binghe.rpc.protocol.RpcProtocol;
import io.binghe.rpc.protocol.header.RpcHeader;
import io.binghe.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author binghe (公众号：冰河技术)
 * @version 1.0.0
 * @description 实现RPC编码
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> implements RpcCodec {

    private FlowPostProcessor postProcessor;

    public RpcEncoder(FlowPostProcessor postProcessor){
        this.postProcessor = postProcessor;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {
        RpcHeader header = msg.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());
        String serializationType = header.getSerializationType();
        Serialization serialization = getSerialization(serializationType);
        byteBuf.writeBytes(SerializationUtils.paddingString(serializationType).getBytes("UTF-8"));
        byte[] data = serialization.serialize(msg.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
        //异步调用流控分析后置处理器
        header.setMsgLen(data.length);
        this.postFlowProcessor(postProcessor, header);
    }
}
