package com.zm.nettydemo.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 服务端 ，处理程序，接收消息
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {


    //存放channel的集合，群聊类似功能
     public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);




    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("服务端接收:"+s);
        //拿到通道，将消息卸写在通道并刷新给客户端
        channelHandlerContext.channel().writeAndFlush("服务端收到你的消息："+s);
        //给每个通道设置msg信息
        for (Channel channel : channels){
            //聊天群里，判断哪个是自己发的消息，哪个是别人发的消息
            if (channel.equals(channelHandlerContext.channel())){//拿到当前通道，循环对比，哪个是当前用户的
                    channel.writeAndFlush("[本人：]"+s);
            }else{
                    channel.writeAndFlush("客户端IP为："+channel.remoteAddress()+"[其他人说：]"+s);
            }
        }


    }

    /**
     * 客户端连接时，会调用这个方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println("用户连接上了");
        //将当前通道放在通道集合中
        channels.add(ctx.channel());

    }

    /**
     * 客户端断开连接时，会调用这个方法
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        System.out.println("用户下线了");
        //将当前通道从通道集合中删除（用户退出时，删除通道）
        channels.remove(ctx.channel());
    }
}
