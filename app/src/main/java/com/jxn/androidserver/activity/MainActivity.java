package com.jxn.androidserver.activity;

import android.app.Activity;
import android.os.Bundle;

import com.jxn.androidserver.R;
import com.jxn.androidserver.server.ResourceInAssetsHandler;
import com.jxn.androidserver.server.SimpleHttpServer;
import com.jxn.androidserver.server.UploadImageHandler;
import com.jxn.androidserver.server.WebConfiguration;

/**
 * Created by jxn on 2016/11/26 0026.
 */

public class MainActivity extends Activity {

    private SimpleHttpServer mSimpleServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebConfiguration webConfig = new WebConfiguration();
        webConfig.setPort(8088);
        webConfig.setMaxParallels(50);
        mSimpleServer = new SimpleHttpServer(webConfig);
        mSimpleServer.registerResourceHandler(new ResourceInAssetsHandler());
        mSimpleServer.registerResourceHandler(new UploadImageHandler());
        mSimpleServer.startAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSimpleServer.stopAsync();
    }
}
