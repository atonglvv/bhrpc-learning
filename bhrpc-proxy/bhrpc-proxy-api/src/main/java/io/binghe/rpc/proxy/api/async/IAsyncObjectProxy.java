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
package io.binghe.rpc.proxy.api.async;

import io.binghe.rpc.proxy.api.future.RPCFuture;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 异步访问接口
 */
public interface IAsyncObjectProxy {

    /**
     * 异步代理对象调用方法
     * @param funcName 方法名称
     * @param args 方法参数
     * @return 封装好的RPCFuture对象
     */
    RPCFuture call(String funcName, Object... args);
}
