package com.zm.nettydemo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

/**
 * 客户端启动类
 */
public class ChatClient {

    public static void main(String[] args) throws InterruptedException, IOException {

        //创建一个线程池
        NioEventLoopGroup group = new NioEventLoopGroup();//连接服务端用的


        try {

            Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChatClientInit());
            Channel channel = bootstrap.connect(new InetSocketAddress("127.0.0.1", 4212)).sync().channel();
            BufferedReader bufferreader = new BufferedReader(new InputStreamReader(System.in));
            while (true){
                channel.writeAndFlush(bufferreader.readLine()+"\r\n");
            }


        }finally {
            group.shutdownGracefully();  //退出线程池
        }


    }
}
