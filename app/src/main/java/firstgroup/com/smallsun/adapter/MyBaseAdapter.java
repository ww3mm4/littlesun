package firstgroup.com.smallsun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by jackiechan on 15/11/9.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
    private List<T> list;
    private Context context;
    private int layoutResId;

    public MyBaseAdapter(List<T> list, Context context, int layoutResId) {
        this.list = list;
        this.context = context;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder = MyViewHolder.getHolder(convertView,context,layoutResId);
            //填充数据
        setData(holder, position);
        return holder.getmView();
    }

    public abstract void setData(MyViewHolder holder, int position);
}
