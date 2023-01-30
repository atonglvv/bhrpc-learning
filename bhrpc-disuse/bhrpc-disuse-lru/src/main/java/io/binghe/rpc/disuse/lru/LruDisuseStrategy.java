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
package io.binghe.rpc.disuse.lru;

import io.binghe.rpc.disuse.api.DisuseStrategy;
import io.binghe.rpc.disuse.api.connection.ConnectionInfo;
import io.binghe.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 判断最近被使用的时间，目前最远的数据优先被淘汰。
 */
@SPIClass
public class LruDisuseStrategy implements DisuseStrategy {
    private final Logger logger = LoggerFactory.getLogger(LruDisuseStrategy.class);
    private final Comparator<ConnectionInfo> lastUseTimeComparator = (o1, o2) -> {
        return o1.getLastUseTime() - o2.getLastUseTime() > 0 ? 1 : -1;
    };
    @Override
    public ConnectionInfo selectConnection(List<ConnectionInfo> connectionList) {
        logger.info("execute lru disuse strategy...");
        if (connectionList.isEmpty()) return null;
        Collections.sort(connectionList, lastUseTimeComparator);
        return connectionList.get(0);
    }
}
