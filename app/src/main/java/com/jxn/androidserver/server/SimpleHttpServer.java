package com.jxn.androidserver.server;

import android.util.Log;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleHttpServer {

    private final ExecutorService mTreadPool;
    private final WebConfiguration mWebConfig;

    private boolean mIsEnable = false;
    private ServerSocket mSocket;

    public SimpleHttpServer(WebConfiguration webConfig){
        this.mWebConfig = webConfig;
        // newCached中一个线程结束之后不会立即把这个线程销毁
        this.mTreadPool =  Executors.newCachedThreadPool();
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
                        Log.d("an-server", "a remote peer accepted" + remotePeer.getRemoteSocketAddress());
                        onAcceptRemotePeer(remotePeer);
                    }
                });
            }
        } catch (IOException e) {
            Log.e("an-server", e.toString());
        }
    }

    /**
     * 处理连接后的操作
     * @param remotePeer
     */
    private void onAcceptRemotePeer(Socket remotePeer) {
        try {
            remotePeer.getOutputStream().write("connected success".getBytes());
        } catch (IOException e) {
            Log.e("an-server", e.toString());
        }
    }
}
