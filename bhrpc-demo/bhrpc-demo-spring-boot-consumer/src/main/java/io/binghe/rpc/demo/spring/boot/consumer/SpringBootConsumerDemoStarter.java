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
package io.binghe.rpc.demo.spring.boot.consumer;

import io.binghe.rpc.demo.spring.boot.consumer.service.ConsumerDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 服务消费者基于SpringBoot的启动类
 */
@SpringBootApplication
@ComponentScan(basePackages = {"io.binghe.rpc"})
public class SpringBootConsumerDemoStarter {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootConsumerDemoStarter.class);
    public static void main(String[] args){
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootConsumerDemoStarter.class, args);
        ConsumerDemoService consumerDemoService = context.getBean(ConsumerDemoService.class);
        String result = consumerDemoService.hello("binghe");
        LOGGER.info("返回的结果数据===>>> " + result);
    }
}
