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
package io.binghe.rpc.test.provider.single;

import io.binghe.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 测试Java原生启动RPC
 */
public class RpcSingleServerTest {

    @Test
    public void startRpcSingleServer(){
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "127.0.0.1:27880","127.0.0.1:2181", "zookeeper", "random","io.binghe.rpc.test", "asm", 3000, 6000, false, 10000, 16, 16, "print", 2, "strategy_default", false, 2, false, "counter", 100, 1000, "exception" /**direct/fallback/exception**/, true, "counter", 1, 5000, "print");
        singleServer.startNettyServer();
    }
}
