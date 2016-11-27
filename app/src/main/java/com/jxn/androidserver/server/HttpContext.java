package com.jxn.androidserver.server;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by jxn on 2016/11/27 0027.
 */

public class HttpContext {

    private final HashMap<String, String> requestHeaders;

    public HttpContext() {
        requestHeaders = new HashMap<String, String>();
    }

    private Socket underlySocket;

    public void setUnderlySocket(Socket underlySocket) {
        this.underlySocket = underlySocket;
    }

    public Socket getUnderlySocket() {
        return underlySocket;
    }

    public void addRequestHeader(String headerKey, String headerValue) {
        requestHeaders.put(headerKey, headerValue);
    }

    public  String getRequestHeaderValue(String headerKey){
        return requestHeaders.get(headerKey);
    }

}
