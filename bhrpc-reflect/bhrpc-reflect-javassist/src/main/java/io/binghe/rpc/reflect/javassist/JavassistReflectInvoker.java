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
package io.binghe.rpc.reflect.javassist;

import io.binghe.rpc.reflect.api.ReflectInvoker;
import io.binghe.rpc.spi.annotation.SPIClass;
import javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author binghe(公众号 ： 冰河技术)
 * @version 1.0.0
 * @description Javassist方式调用方法
 */
@SPIClass
public class JavassistReflectInvoker implements ReflectInvoker {
    private final Logger logger = LoggerFactory.getLogger(JavassistReflectInvoker.class);
    @Override
    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        logger.info("use javassist reflect type invoke method...");
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(serviceClass);
        Class<?> childClass = proxyFactory.createClass();
        Method method = childClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(childClass.newInstance(), parameters);
    }
}
