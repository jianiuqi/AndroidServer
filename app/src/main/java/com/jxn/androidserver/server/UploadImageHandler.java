package com.jxn.androidserver.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Created by jxn on 2016/11/27 0027.
 */

public class UploadImageHandler implements IUriResourceHandler {


    private  String mAcceptPrefix = "/upload_image/";

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(mAcceptPrefix);//以prefix结尾时返回true
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        OutputStream os = httpContext.getUnderlySocket().getOutputStream();
        PrintWriter writer = new PrintWriter(os);
        writer.println("HTTP/1.1 200 OK");
        //输出\r\n (不能写错)
        writer.println();
        writer.println("from upload image handler");
        writer.flush();
    }
}
