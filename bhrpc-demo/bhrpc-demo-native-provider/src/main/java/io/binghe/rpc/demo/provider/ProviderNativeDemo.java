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
package io.binghe.rpc.demo.provider;

import io.binghe.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 服务提供者
 */
public class ProviderNativeDemo {

    @Test
    public void startRpcSingleServer(){
        RpcSingleServer singleServer = new RpcSingleServer("127.0.0.1:27880", "127.0.0.1:27880","127.0.0.1:2181", "zookeeper", "random","io.binghe.rpc.demo", "asm", 3000, 6000, true, 30000, 16, 16, "print", 1, "refuse", true, 2);
        singleServer.startNettyServer();
    }
}
