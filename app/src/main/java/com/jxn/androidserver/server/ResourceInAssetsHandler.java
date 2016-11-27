package com.jxn.androidserver.server;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class ResourceInAssetsHandler implements IUriResourceHandler {

    private  String mAcceptPrefix = "/static/";

    private Context mContext;

    public  ResourceInAssetsHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(mAcceptPrefix);//以prefix结尾时返回true
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        int startIndex = mAcceptPrefix.length();
        String assetsPath = uri.substring(startIndex);
        InputStream in = mContext.getAssets().open(assetsPath);
        byte[] raw = StreamToolKit.readRawFromStream(in);
        in.close();
        OutputStream os = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printStream = new PrintStream(os); // 包装流
        printStream.println("HTTP/1.1 200 OK");
        printStream.println("Content-Length:" + raw.length);
        if (assetsPath.endsWith(".html")) {
            printStream.println("Content-Type:text/html");
        }else if (assetsPath.endsWith(".js")) {
            printStream.println("Content-Type:text/js");
        }else if (assetsPath.endsWith(".css")) {
            printStream.println("Content-Type:text/css");
        }else if (assetsPath.endsWith(".jpg")) {
            printStream.println("Content-Type:text/jpg");
        }else if (assetsPath.endsWith(".png")) {
            printStream.println("Content-Type:text/png");
        }
        printStream.println();
        printStream.write(raw);
    }
}
