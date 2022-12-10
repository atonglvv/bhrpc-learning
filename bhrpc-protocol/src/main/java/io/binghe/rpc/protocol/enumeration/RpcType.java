package io.binghe.rpc.protocol.enumeration;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description 协议的类型
 */
public enum RpcType {
    //请求消息
    REQUEST(1),
    //响应消息
    RESPONSE(2),
    //从服务消费者发起的心跳数据
    HEARTBEAT_FROM_CONSUMER(3),
    //服务提供者响应服务消费者的心跳数据
    HEARTBEAT_TO_CONSUMER(4),
    //从服务提供者发起的心跳数据
    HEARTBEAT_FROM_PROVIDER(5),
    //服务消费者响应服务提供者的心跳数据
    HEARTBEAT_TO_PROVIDER(6);

    private final int type;

    RpcType(int type) {
        this.type = type;
    }

    public static RpcType findByType(int type) {
        for (RpcType rpcType : RpcType.values()) {
            if (rpcType.getType() == type) {
                return rpcType;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }
}