package com.jxn.androidserver.server;

/**
 * Created by jxn on 2016/11/26 0026.
 */

public class WebConfiguration {

    /**
     * 端口号
     */
    private  int port;

    /**
     * 最大并发数
     */
    private int maxParallels;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }
}
