package firstgroup.com.smallsun.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import imsdk.data.mainphoto.IMSDKMainPhoto;

/**
 * Created by mugua on 2015/11/18.
 */
public class CustomAsyTask extends AsyncTask<String,Void,Bitmap>{
    private ViewGroup group;
    private String key;

    public CustomAsyTask(ViewGroup group) {
        this.group = group;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        key = params[0];
        Bitmap bitmap = MyLruCache.getLruCache().get(params[0]);
        if (bitmap==null) {

            return IMSDKMainPhoto.get(key);
        }
        else {
            Log.i("ww3","---"+"bitmap");
            return bitmap;
        }

    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        View view = group.findViewWithTag(key);
        if (view!=null){
            ImageView imageView = ((ImageView) view);
            imageView.setImageBitmap(bitmap);
            imageView.setTag("");
        }
        if (bitmap!=null)
        MyLruCache.getLruCache().put(key,bitmap);
    }
}
