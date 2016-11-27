package com.jxn.androidserver.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/27 0027.
 */

public class StreamToolKit {

    public static final String readLine(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c1 = 0;
        int c2 = 0;
        // c2=-1表示遍历到流的结尾
        while (c2 != -1 && !(c1 == '\r' && c2 == '\n')) {
            c1 = c2;
            c2 = in.read();
            sb.append((char) c2);
        }
        if (sb.length() == 0)
            return null;
        return  sb.toString();
    }

}
