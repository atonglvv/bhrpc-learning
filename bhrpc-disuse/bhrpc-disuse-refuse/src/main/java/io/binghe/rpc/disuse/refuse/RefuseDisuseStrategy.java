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
package io.binghe.rpc.disuse.refuse;

import io.binghe.rpc.common.exception.RefuseException;
import io.binghe.rpc.disuse.api.DisuseStrategy;
import io.binghe.rpc.disuse.api.connection.ConnectionInfo;
import io.binghe.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 拒绝新连接
 */
@SPIClass
public class RefuseDisuseStrategy implements DisuseStrategy {
    private final Logger logger = LoggerFactory.getLogger(RefuseDisuseStrategy.class);
    @Override
    public ConnectionInfo selectConnection(List<ConnectionInfo> connectionList) {
        logger.info("execute refuse disuse strategy...");
        throw new RefuseException("refuse new connection...");
    }
}
