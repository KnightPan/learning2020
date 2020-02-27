package com.stoneknife.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioServer {
    public static void main(String[] args) {

        //创建有限个数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(3);

        //创建一个selector
        try (Selector selector = Selector.open();) {

            //创建通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            int port = 9200;
            ssc.bind(new InetSocketAddress(port));

            //注册到selector,让selector帮忙检测连接是否进来
            //1、申明自己为非阻塞方式
            ssc.configureBlocking(false);

            //2、注册
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            //交给主线程 --》负责选就绪的通道
            while(true) {
                //阻塞选择就绪事件 select()方法可以被中断
                int readyChannelCount = selector.select();
                //select()方法可以被中断
                if(readyChannelCount == 0)
                    continue;
                //得到就绪的channel
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if(selectionKey.isAcceptable()) {
                        //有连接就绪
                        //接收连接
                        SocketChannel sc = ssc.accept();

                        //交给selector检测数据是否来了(注册到seletor)
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);

                    }else if(selectionKey.isReadable()) {
                        //数据已经发送过来,需要去处理
                        //将数据交给线程池读取数据处理
                        pool.submit(new SocketProgress(selectionKey));

                        //取消selector注册,防止异步线程池处理不及时，被重复选择
                        selectionKey.cancel();

                    }else if(selectionKey.isWritable()){
                        //数据可以往外写
                    }else if(selectionKey.isConnectable()) {
                        //客户端连接到服务器
                    }

                    iterator.remove();//处理了，一定要从iterator中移除
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Charset charset = Charset.forName("UTF-8");
    private static CharsetDecoder decoder = charset.newDecoder();

    static class SocketProgress implements Runnable {

        SelectionKey selectionKey;

        public  SocketProgress(SelectionKey selectionKey) {
            super();
            this.selectionKey = selectionKey;
        }

        @Override
        public void run() {

            try(SocketChannel channel = (SocketChannel)selectionKey.channel();) {
                //读数据
                //1、创建buffer？？？如何读取不定量数据超过1024字节
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                //readBytes == -1时表示读完了
                int readBytes = channel.read(buffer);

                //将buffer转成读模式
                buffer.flip();

                String message = decoder.decode(buffer).toString();

                System.out.println(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
