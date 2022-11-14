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
package io.binghe.rpc.reflect.api;

import io.binghe.rpc.spi.annotation.SPI;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 反射方法的调用接口
 */
@SPI
public interface ReflectInvoker {

    /**
     * 调用真实方法的SPI通用接口
     * @param serviceBean 方法所在的对象实例
     * @param serviceClass 方法所在对象实例的Class对象
     * @param methodName 方法的名称
     * @param parameterTypes 方法的参数类型数组
     * @param parameters 方法的参数数组
     * @return 方法调用的结果信息
     * @throws Throwable 抛出的异常
     */
    Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable;
}
