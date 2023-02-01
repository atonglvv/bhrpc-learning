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
package io.binghe.rpc.buffer.object;

import io.binghe.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;

import java.io.Serializable;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 缓冲对象
 */
public class BufferObject<T> implements Serializable {

    private static final long serialVersionUID = -5465112244213170405L;

    //Netty读写数据的ChannelHandlerContext
    private ChannelHandlerContext ctx;

    //网络传输协议对象
    private RpcProtocol<T> protocol;

    public BufferObject() {
    }

    public BufferObject(ChannelHandlerContext ctx, RpcProtocol<T> protocol) {
        this.ctx = ctx;
        this.protocol = protocol;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public RpcProtocol<T> getProtocol() {
        return protocol;
    }

    public void setProtocol(RpcProtocol<T> protocol) {
        this.protocol = protocol;
    }
}
