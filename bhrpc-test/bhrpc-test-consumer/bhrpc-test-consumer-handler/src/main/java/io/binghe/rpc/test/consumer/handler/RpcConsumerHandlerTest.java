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
package io.binghe.rpc.test.consumer.handler;

import io.binghe.rpc.consumer.common.RpcConsumer;
import io.binghe.rpc.consumer.common.callback.AsyncRPCCallback;
import io.binghe.rpc.consumer.common.context.RpcContext;
import io.binghe.rpc.consumer.common.future.RPCFuture;
import io.binghe.rpc.protocol.RpcProtocol;
import io.binghe.rpc.protocol.header.RpcHeaderFactory;
import io.binghe.rpc.protocol.request.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 测试服务消费者
 */
public class RpcConsumerHandlerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcConsumerHandlerTest.class);

    public static void main(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RPCFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocol());
        rpcFuture.addCallback(new AsyncRPCCallback() {
            @Override
            public void onSuccess(Object result) {
                LOGGER.info("从服务消费者获取到的数据===>>>" + result);
            }

            @Override
            public void onException(Exception e) {
                LOGGER.info("抛出了异常===>>>" + e);
            }
        });
        Thread.sleep(200);
        consumer.close();
    }

    public static void mainAsync(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        consumer.sendRequest(getRpcRequestProtocol());
        RPCFuture future = RpcContext.getContext().getRPCFuture();
        LOGGER.info("从服务消费者获取到的数据===>>>" + future.get());
        consumer.close();
    }

    public static void mainSync(String[] args) throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RPCFuture future = consumer.sendRequest(getRpcRequestProtocol());
        LOGGER.info("从服务消费者获取到的数据===>>>" + future.get());
        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocol(){
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("io.binghe.rpc.test.api.DemoService");
        request.setGroup("binghe");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"binghe"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocolAsync(){
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("io.binghe.rpc.test.api.DemoService");
        request.setGroup("binghe");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"binghe"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(true);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocolSync(){
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("io.binghe.rpc.test.api.DemoService");
        request.setGroup("binghe");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"binghe"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }
}
