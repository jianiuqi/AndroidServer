package com.jxn.androidserver.server;

import android.content.Context;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Created by jxn on 2016/11/27 0027.
 */

public class UploadImageHandler implements IUriResourceHandler {


    private  String mAcceptPrefix = "/upload_image/";

    private  Context mContext;

    public UploadImageHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public boolean accept(String uri) {
        return uri.startsWith(mAcceptPrefix);//以prefix结尾时返回true
    }

    @Override
    public void handle(String uri, HttpContext httpContext) throws IOException {
        String tempPath = "/mnt/sdcard/test_upload.jpg";
        Long totalLength = Long.valueOf(httpContext.getRequestHeaderValue("Content-Length"));// 收取到的文件的长度
        FileOutputStream fos = new FileOutputStream(tempPath);
        InputStream in = httpContext.getUnderlySocket().getInputStream();
        byte[] buffer = new byte[10240];
        int nReaded = 0; //从流读到buffer中实际的字节长度
        long nLeftLength = totalLength;// 剩余还需要读的字节数
        while ((nReaded = in.read(buffer)) > 0 && nLeftLength > 0) {
            fos.write(buffer, 0, nReaded);
            nLeftLength -= nReaded;
        }
        fos.close();

        OutputStream os = httpContext.getUnderlySocket().getOutputStream();
        PrintStream printStream = new PrintStream(os);
        printStream.println("HTTP/1.1 200 OK");
        //输出\r\n (不能写错)
        printStream.println();

        onImageLoaded(tempPath);
        printStream.println("received");
        printStream.close();
    }

    protected void onImageLoaded(String path){

    }
}
