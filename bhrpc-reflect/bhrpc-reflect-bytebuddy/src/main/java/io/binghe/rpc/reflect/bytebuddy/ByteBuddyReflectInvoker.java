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
package io.binghe.rpc.reflect.bytebuddy;

import io.binghe.rpc.reflect.api.ReflectInvoker;
import io.binghe.rpc.spi.annotation.SPIClass;
import net.bytebuddy.ByteBuddy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description ByteBuddy反射机制
 */
@SPIClass
public class ByteBuddyReflectInvoker implements ReflectInvoker {
    private final Logger logger = LoggerFactory.getLogger(ByteBuddyReflectInvoker.class);
    @Override
    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {
        logger.info("use bytebuddy reflect type invoke method...");
        Class<?> childClass = new ByteBuddy().subclass(serviceClass)
                .make()
                .load(ByteBuddyReflectInvoker.class.getClassLoader())
                .getLoaded();
        Object instance = childClass.getDeclaredConstructor().newInstance();
        Method method = childClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(instance, parameters);
    }
}
