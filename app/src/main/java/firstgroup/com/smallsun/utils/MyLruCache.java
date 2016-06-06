package firstgroup.com.smallsun.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by mugua on 2015/11/18.
 */
public class MyLruCache {
    private static LruCache<String,Bitmap> lruCache ;

    private MyLruCache() {

    }
    public static LruCache<String,Bitmap> getLruCache(){
        if (lruCache==null){
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int cacheSize = maxMemory / 8;
            lruCache = new LruCache<String,Bitmap>(cacheSize);
        }
        return lruCache;
    }

}
