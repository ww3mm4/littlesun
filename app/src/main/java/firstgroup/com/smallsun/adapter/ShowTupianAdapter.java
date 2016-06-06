package firstgroup.com.smallsun.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import firstgroup.com.smallsun.R;
import imsdk.data.community.IMCommunity;

/**
 * Created by Cherrys on 2015/11/19.
 */
public class ShowTupianAdapter extends BaseAdapter{
    private String[] data;
    private Context context;
    public ShowTupianAdapter(String[] data,Context context){
        this.data = data;
        this.context = context;
    }
    @Override
    public int getCount() {
        return data!=null?data.length:0;
    }

    @Override
    public Object getItem(int position) {
        return data!=null?data[position]:null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.griditem_addpic,null);
        final ImageView iv = (ImageView) view.findViewById(R.id.imageView1);
        iv.setImageResource(R.drawable.iv_bg);
        String url = data[position];
        IMCommunity.downloadImage(url, 80, 80, new IMCommunity.onDownloadImageListener() {
            @Override
            public void onSuccess(String s, Bitmap bitmap) {
                iv.setImageBitmap(bitmap);
            }

            @Override
            public void onProgress(double v) {
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(context,"加载失败",Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
