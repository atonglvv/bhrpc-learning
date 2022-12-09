package io.binghe.rpc.protocol.header;

import io.binghe.rpc.common.id.IdFactory;
import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.protocol.enumeration.RpcType;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description RpcHeaderFactory
 */
public class RpcHeaderFactory {

    public static RpcHeader getRequestHeader(String serializationType, int messageType){
        RpcHeader header = new RpcHeader();
        long requestId = IdFactory.getId();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        header.setMsgType((byte) messageType);
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializationType);
        return header;
    }
}