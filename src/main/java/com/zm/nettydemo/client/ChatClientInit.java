package com.zm.nettydemo.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 客户端 初始化程序
 */
public class ChatClientInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //先创建一个管道
        ChannelPipeline pieline = socketChannel.pipeline();

        //设置编码格式
        pieline.addLast("decoder",new StringDecoder());
        //设置解码
        pieline.addLast("encoder",new StringEncoder());
        //设置处理程序
        pieline.addLast("handler",new ChatClientHandler());

        System.out.println("客户端初始化程序-----init调用了");

    }
}
