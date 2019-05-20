package com.zm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *  新 NIO编程
 */
public class NioSocket {
    //定义通道选择器（相当调度工作的） 因为所有线程都会用到（共用的），所以定义成全局的
    private Selector selector;


    public static void main(String[] args) throws IOException {

          NioSocket nioSocket = new NioSocket();
          nioSocket.init(2421);
          nioSocket.listenSelector();

    }

    //初始化server
    public void init(int port) throws IOException {
        //新建NIO通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //使通道设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        //新建socket通道的端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        //初始化Selector
        this.selector = Selector.open();
        //通道和选择器进行绑定（//将通道注册到选择器上去。SelectionKey.OP_ACCEPT：监听器（时间），监听连接事件）
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("服务已启动......");


    }

    //监听选择器（selector）的方法
    public void listenSelector() throws IOException {
        //服务器轮询监听selector
        while (true){
            //等待连接
            //select模型，多路复用
            this.selector.select();
            System.out.println("有客户端连接了。。。");
            //遍历selector中的事件
            Iterator<SelectionKey> itse = selector.selectedKeys().iterator();
            while (itse.hasNext()){
                SelectionKey key = itse.next();//获取事件
                //将事件移除
                itse.remove();
                //处理请求
                handle(key);

            }
        }


    }

    /**
     * 处理客户端请求
     * @param key
     */
    private void handle(SelectionKey key) throws IOException {
        //如果是连接事件
        if (key.isAcceptable()){
            //处理客户端连接事件
            //获取客户端的连接通道
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            //将管道设置为非租塞（no-locking）
            socketChannel.configureBlocking(false);
            //接受客户端发送的信息时，需要给通道设置读的权限
            socketChannel.register(selector,SelectionKey.OP_READ);
        }else if(key.isReadable()) {
           //处理读的事件
            SocketChannel socketChanel = (SocketChannel) key.channel();
            ByteBuffer buff = ByteBuffer.allocate(1024);//初始化bytebuff的容量
            //读到的位置
            int bb = socketChanel.read(buff);
            if(bb>0){
                //将读到的字节转化为字符串
                String s = new String(buff.array(),"GBK").trim();
                System.out.println("服务端收到的数据"+s);
            }else{
                System.out.println("数据已读完,客户端关闭");
                key.cancel();
            }

        }
    }

}
