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
package io.binghe.rpc.test.registry;

import io.binghe.rpc.protocol.meta.ServiceMeta;
import io.binghe.rpc.registry.api.RegistryService;
import io.binghe.rpc.registry.api.config.RegistryConfig;
import io.binghe.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 注册测试
 */
public class ZookeeperRegistryTest {

    private RegistryService registryService;

    private ServiceMeta serviceMeta;

    @Before
    public void init() throws Exception{
        RegistryConfig registryConfig = new RegistryConfig("127.0.0.1:2181", "zookeeper", "random");
        this.registryService = new ZookeeperRegistryService();
        this.registryService.init(registryConfig);
        this.serviceMeta = new ServiceMeta(ZookeeperRegistryTest.class.getName(), "1.0.0", "binghe", "127.0.0.1", 8080);
    }

    @Test
    public void testRegister() throws Exception {
        this.registryService.register(serviceMeta);
    }

    @Test
    public void testUnRegister() throws Exception {
        this.registryService.unRegister(serviceMeta);
    }

    @Test
    public void testDiscovery() throws Exception {
        this.registryService.discovery(RegistryService.class.getName(), "binghe".hashCode());
    }

    @Test
    public void testDestroy() throws IOException {
        this.registryService.destroy();
    }
}
