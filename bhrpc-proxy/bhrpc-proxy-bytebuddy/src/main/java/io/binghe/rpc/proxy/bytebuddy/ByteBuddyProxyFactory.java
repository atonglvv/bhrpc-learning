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
package io.binghe.rpc.proxy.bytebuddy;

import io.binghe.rpc.proxy.api.BaseProxyFactory;
import io.binghe.rpc.proxy.api.ProxyFactory;
import io.binghe.rpc.spi.annotation.SPIClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description ByteBuddy动态代理
 */
@SPIClass
public class ByteBuddyProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {
    private final Logger logger = LoggerFactory.getLogger(ByteBuddyProxyFactory.class);
    @Override
    public <T> T getProxy(Class<T> clazz) {
       try{
           logger.info("基于ByteBuddy动态代理...");
           return (T) new ByteBuddy().subclass(Object.class)
                   .implement(clazz)
                   .intercept(InvocationHandlerAdapter.of(objectProxy))
                   .make()
                   .load(ByteBuddyProxyFactory.class.getClassLoader())
                   .getLoaded()
                   .getDeclaredConstructor()
                   .newInstance();
       }catch (Exception e){
           logger.error("bytebuddy proxy throws exception:{}", e);
       }
       return null;
    }
}
