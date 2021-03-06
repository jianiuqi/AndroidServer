package com.jxn.androidserver.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private final ExecutorService mTreadPool;
    private final WebConfiguration mWebConfig;

    private boolean mIsEnable = false;
    private ServerSocket mSocket;
    private Set<IUriResourceHandler> mResourceHandlers;

    public SimpleHttpServer(WebConfiguration webConfig){
        this.mWebConfig = webConfig;
        // newCached中一个线程结束之后不会立即把这个线程销毁
        this.mTreadPool =  Executors.newCachedThreadPool();
        this.mResourceHandlers = new HashSet<IUriResourceHandler>();
    }

    /**
     * 启动Server（异步）
     */
    public void startAsync(){
        mIsEnable = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                doProcessASync();
            }
        }).start();
    }

    /**
     * 停止Server（异步）
     */
    public  void stopAsync(){
        if (!mIsEnable)
            return;
        mIsEnable = false;
        try {
            mSocket.close();
            mSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doProcessASync() {
        try {
            InetSocketAddress socketAddr = new InetSocketAddress(mWebConfig.getPort());
            mSocket = new ServerSocket();
            mSocket.bind(socketAddr);
            while (mIsEnable) {
                // accept是一个阻塞的方法，当有连接时才会返回Socket值
                final Socket remotePeer = mSocket.accept();
                // 每当有一个接入时就放到线程池中处理
                mTreadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("a remote peer accepted" + remotePeer.getRemoteSocketAddress());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            Log.e("an-server", e.toString());
        }
    }

    public  void  registerResourceHandler(IUriResourceHandler handler){
        mResourceHandlers.add(handler);
    }

    /**
     * 处理连接后的操作
     * @param remotePeer
     */
    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
//            remotePeer.getOutputStream().write("connected success".getBytes());
            HttpContext httpContext = new HttpContext();
            httpContext.setUnderlySocket(remotePeer);
            InputStream in = remotePeer.getInputStream();
            String headerLine = null;
            String resourceUri = StreamToolKit.readLine(in).split(" ")[1];
            System.out.print("resourceUri is :" + resourceUri);
            while ((headerLine = StreamToolKit.readLine(in)) != null){
                // 头数据会以两个\r\n结尾
                if (headerLine.equals("\r\n"))
                    break;
                System.out.println("headers is :" + headerLine);
                String[] pair = headerLine.split(": ");
                httpContext.addRequestHeader(pair[0], pair[1]);
            }

            for (IUriResourceHandler handler : mResourceHandlers) {
                if (!handler.accept(resourceUri))
                    continue;
                handler.handle(resourceUri, httpContext);
            }
        } catch (IOException e) {
            Log.e("an-server", e.toString());
        }
    }
}
