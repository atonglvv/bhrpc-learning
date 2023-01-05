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
package io.binghe.rpc.consumer.spring.context;

import org.springframework.context.ApplicationContext;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description Spring上下文
 */
public class RpcConsumerSpringContext {
    /**
     * Spring ApplicationContext
     */
    private ApplicationContext context;

    private RpcConsumerSpringContext(){

    }

    private static class Holder{
        private static final RpcConsumerSpringContext INSTANCE = new RpcConsumerSpringContext();
    }

    public static RpcConsumerSpringContext getInstance(){
        return Holder.INSTANCE;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }
}
