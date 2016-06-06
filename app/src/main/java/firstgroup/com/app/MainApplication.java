package firstgroup.com.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.logging.Handler;

import firstgroup.com.smallsun.R;
import imsdk.data.IMSDK;

/**
 * Created by mugua on 2015/11/15.
 */
public class MainApplication extends Application {
    //
    //imsdk的key值
    private static final String sAppKey = "2e39957046dc259144d7e0d5";
    private static RequestQueue mQueue;
    public void onCreate() {
        super.onCreate();
        IMSDK.init(getApplicationContext(), sAppKey);
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }
    public static RequestQueue getHttpQueue() {
        return mQueue;
    }

}
