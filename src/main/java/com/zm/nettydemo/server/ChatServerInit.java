package com.zm.nettydemo.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 服务端 初始化程序
 */
public class ChatServerInit extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //先创建一个管道
        ChannelPipeline pieline = socketChannel.pipeline();

        //设置编码格式
        pieline.addLast("decoder",new StringDecoder());
        //设置解码
        pieline.addLast("encoder",new StringEncoder());
        //设置处理程序
        pieline.addLast("handler",new ChatServerHandler());

        System.out.println("服务端初始化程序-----init调用了");

    }
}
