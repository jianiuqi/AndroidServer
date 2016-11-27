package com.jxn.androidserver.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class ResourceInAssetsHandler implements IUriResourceHandler {

    private  String mAcceptPrefix = "/static/";

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(mAcceptPrefix);//以prefix结尾时返回true
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        OutputStream os = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter writer = new PrintWriter(os);
        writer.println("HTTP/1.1 200 OK");
        writer.println();
        writer.println("from resource in assets handler");
        writer.flush();
    }
}
