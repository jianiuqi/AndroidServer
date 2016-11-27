package com.jxn.androidserver.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.jxn.androidserver.R;
import com.jxn.androidserver.server.ResourceInAssetsHandler;
import com.jxn.androidserver.server.SimpleHttpServer;
import com.jxn.androidserver.server.UploadImageHandler;
import com.jxn.androidserver.server.WebConfiguration;

import static android.R.attr.bitmap;

/**
 * Created by jxn on 2016/11/26 0026.
 */

public class MainActivity extends Activity {

    private SimpleHttpServer mSimpleServer;

    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image_show);

        WebConfiguration webConfig = new WebConfiguration();
        webConfig.setPort(8088);
        webConfig.setMaxParallels(50);
        mSimpleServer = new SimpleHttpServer(webConfig);
        mSimpleServer.registerResourceHandler(new ResourceInAssetsHandler(this));
        mSimpleServer.registerResourceHandler(new UploadImageHandler(this){
            @Override
            protected void onImageLoaded(String path) {
                showImage(path);
            }
        });
        mSimpleServer.startAsync();
    }

    private void showImage(final String path) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                mImageView.setImageBitmap(bitmap);
                Toast.makeText(MainActivity.this, "image received and shown", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSimpleServer.stopAsync();
    }
}
