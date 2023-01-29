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
package io.binghe.rpc.connection.manager;

import io.binghe.rpc.common.exception.RefuseException;
import io.binghe.rpc.common.utils.StringUtils;
import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.disuse.api.DisuseStrategy;
import io.binghe.rpc.disuse.api.connection.ConnectionInfo;
import io.binghe.rpc.spi.loader.ExtensionLoader;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 连接管理器
 */
public class ConnectionManager {

    private volatile Map<String, ConnectionInfo> connectionMap = new ConcurrentHashMap<>();
    private final DisuseStrategy disuseStrategy;
    private final int maxConnections;
    private static volatile ConnectionManager instance;

    private ConnectionManager(int maxConnections, String disuseStrategyType){
        this.maxConnections = maxConnections <= 0 ? Integer.MAX_VALUE : maxConnections;
        disuseStrategyType = StringUtils.isEmpty(disuseStrategyType) ? RpcConstants.RPC_CONNECTION_DISUSE_STRATEGY_DEFAULT : disuseStrategyType;
        this.disuseStrategy = ExtensionLoader.getExtension(DisuseStrategy.class, disuseStrategyType);
    }

    /**
     * 单例模式
     */
    public static ConnectionManager getInstance(int maxConnections, String disuseStrategyType){
        if (instance == null){
            synchronized (ConnectionManager.class){
                if (instance == null){
                    instance = new ConnectionManager(maxConnections, disuseStrategyType);
                }
            }
        }
        return instance;
    }

    /**
     * 添加连接
     */
    public void add(Channel channel){
        ConnectionInfo info = new ConnectionInfo(channel);
        if (this.checkConnectionList(info)){
            connectionMap.put(getKey(channel), info);
        }
    }

    /**
     * 移除连接
     */
    public void remove(Channel channel){
        connectionMap.remove(getKey(channel));
    }

    /**
     * 更新连接信息
     */
    public void update(Channel channel){
        ConnectionInfo info = connectionMap.get(getKey(channel));
        info.setLastUseTime(System.currentTimeMillis());
        info.incrementUseCount();
        connectionMap.put(getKey(channel), info);
    }

    /**
     * 检测连接列表
     */
    private boolean checkConnectionList(ConnectionInfo info) {
        List<ConnectionInfo> connectionList = new ArrayList<>(connectionMap.values());
        if (connectionList.size() >= maxConnections){
           try{
               ConnectionInfo cacheConnectionInfo = disuseStrategy.selectConnection(connectionList);
               if (cacheConnectionInfo != null){
                   cacheConnectionInfo.getChannel().close();
                   connectionMap.remove(getKey(cacheConnectionInfo.getChannel()));
               }
           }catch (RefuseException e){
               info.getChannel().close();
               return false;
           }
        }
        return true;
    }


    private String getKey(Channel channel){
        return channel.id().asLongText();
    }
}
