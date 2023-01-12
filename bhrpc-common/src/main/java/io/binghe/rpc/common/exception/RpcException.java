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
package io.binghe.rpc.common.exception;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description RpcException
 */
public class RpcException extends RuntimeException {
    private static final long serialVersionUID = -6783134254669118520L;

    /**
     * Instantiates a new Serializer exception.
     *
     * @param e the e
     */
    public RpcException(final Throwable e) {
        super(e);
    }

    /**
     * Instantiates a new Serializer exception.
     *
     * @param message the message
     */
    public RpcException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new Serializer exception.
     *
     * @param message   the message
     * @param throwable the throwable
     */
    public RpcException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
