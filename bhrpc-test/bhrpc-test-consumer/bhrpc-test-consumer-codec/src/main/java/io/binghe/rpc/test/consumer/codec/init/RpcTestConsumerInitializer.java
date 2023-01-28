package io.binghe.rpc.test.consumer.codec.init;

import io.binghe.rpc.codec.RpcDecoder;
import io.binghe.rpc.codec.RpcEncoder;
import io.binghe.rpc.flow.processor.FlowPostProcessor;
import io.binghe.rpc.test.consumer.codec.handler.RpcTestConsumerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * @author binghe
 * @version 1.0.0
 * @description
 */
public class RpcTestConsumerInitializer extends ChannelInitializer<SocketChannel> {
    private FlowPostProcessor flowPostProcessor;

    public RpcTestConsumerInitializer(FlowPostProcessor flowPostProcessor){
        this.flowPostProcessor = flowPostProcessor;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline cp = socketChannel.pipeline();
        cp.addLast(new RpcEncoder(flowPostProcessor));
        cp.addLast(new RpcDecoder(flowPostProcessor));
        cp.addLast(new RpcTestConsumerHandler());
    }
}