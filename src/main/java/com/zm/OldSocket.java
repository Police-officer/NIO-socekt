package com.zm;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统socket IO编程
 */
public class OldSocket {


    public static void main(String[] args) throws IOException {

        // 创建多线程 线程池
        ExecutorService threadpool = Executors.newCachedThreadPool();
        //相当于一个Server服务   访问： ip:port
        ServerSocket serverSocket = new ServerSocket(1421);
        System.out.println("服务已启动......");

        while (true) {

            //获取socket客户端套接字(ip:port)
            final Socket socket = serverSocket.accept();//这里是第一个阻塞点
            threadpool.execute(new Runnable() {
                public void run() {
                    try {
                        System.out.println("有客户端连接了......");
                        //客户端发给服务端数据，服务端接受（获取客户端的输入流）
                        InputStream in = socket.getInputStream();
                        //1kb
                        byte[] b = new byte[1024];
                        //循环客户端的输入流
                        while (true){
                            // 批量读取客户端的输入流，一次读1024个字节（1kb）
                            int aa = in.read(b);//这是第二个阻塞点
                            System.out.println("in.read(b)返回的数据是什么======="+aa);
                            if (aa != -1){
                                String s = new String(b,0,aa,"GBK");//设置响应编码
                                System.out.println("这是字节转成字符串后的输出"+s);
                            }else{
                                //数据读完了
                                break;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

    }
}
