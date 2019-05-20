package com.zm.nettydemo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * server 启动程序
 */
public class ChatServer {

    public static void main(String[] args) throws InterruptedException {
         //创建两个线程池
        NioEventLoopGroup boos = new NioEventLoopGroup();//处理 连接 用的
        NioEventLoopGroup works = new NioEventLoopGroup();//处理具体事件 用的

        try {

            //创建一个服务端
            ServerBootstrap serverbootstrap = new ServerBootstrap();

            //将线程池添加到服务中             配置channel                        配置处理程序
            serverbootstrap.group(boos,works).channel(NioServerSocketChannel.class).childHandler(new ChatServerInit())
                    .option(ChannelOption.SO_BACKLOG,128).childOption(ChannelOption.SO_KEEPALIVE,true);
            //配置最大连接数128个                        设置连接状态为 保持连接（长连接，一直保持会话）

            System.out.println("服务端启动了......");

            //绑定端口,开始接收进来的连接；为防止程序挂掉，设置为异步
            ChannelFuture chac = serverbootstrap.bind(4212).sync();
            //等待服务器socket关闭
            //在本例子中不会发生,这时可以关闭服务器了
            chac.channel().closeFuture().sync();

        }finally {

            //关掉服务
            boos.shutdownGracefully();
            works.shutdownGracefully();

        }


    }
}
