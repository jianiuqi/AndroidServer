package com.jxn.androidserver;

import java.net.InetSocketAddress;

public class SimpleHttpServer {

    WebConfiguration mWebConfig;

    boolean mIsEnable = false;

    public SimpleHttpServer(WebConfiguration webConfig){
        this.mWebConfig = webConfig;
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

    }

    private void doProcessASync() {
        InetSocketAddress socketAddr = new InetSocketAddress(mWebConfig.getPort());

    }
}
