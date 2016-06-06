package firstgroup.com.smallsun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import firstgroup.com.smallsun.R;

/**
 * Created by Cherrys on 2015/11/18.
 */
public class Qun_ListAdapter  extends BaseAdapter{
    private List<String> data;
    private Context context;
    private List<String> info_list;
    public Qun_ListAdapter(List<String> data,Context context,List<String> info_list){
        this.context = context;
        this.data = data;
        this.info_list = info_list;
    }
    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public Object getItem(int position) {
        return data!=null?data.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.qun_list_item,null);
            vh = new ViewHolder();
            vh.tvName = ((TextView) convertView.findViewById(R.id.q_tvName));

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tvName.setText(data.get(position));
        return convertView;
    }
    class ViewHolder{
        private TextView tvName;
    }
}
