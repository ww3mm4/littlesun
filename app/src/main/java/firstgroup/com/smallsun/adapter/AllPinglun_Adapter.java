package firstgroup.com.smallsun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import firstgroup.com.smallsun.R;
import imsdk.data.community.CMCommInfo;

/**
 * Created by Cherrys on 2015/11/18.
 */
public class AllPinglun_Adapter extends BaseAdapter {
    private List<CMCommInfo> data;
    private Context context;
    public AllPinglun_Adapter(List<CMCommInfo> data,Context context) {
        this.data = data;
        this.context = context;
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
        Vh vh;
        if (convertView==null){
            vh = new Vh();
            convertView = LayoutInflater.from(context).inflate(R.layout.all_pinglun_item,null);
            vh.tvContent = (TextView) convertView.findViewById(R.id.p_content);
            vh.tvName = (TextView) convertView.findViewById(R.id.p_userName);
            vh.tvTime = (TextView) convertView.findViewById(R.id.p_time);
            vh.userIv = (CircleImageView) convertView.findViewById(R.id.p_userIv);

            convertView.setTag(vh);
        }else{
            vh = (Vh) convertView.getTag();
        }
        CMCommInfo info = data.get(position);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        vh.tvTime.setText(df.format(new Date(info.getCommTime()*1000)));
        vh.tvName.setText(info.getCommCreator().getCustomUserID());
        vh.tvContent.setText(info.getCommContent());

        return convertView;
    }
    class Vh{
        private TextView tvName,tvTime,tvContent;
        private CircleImageView userIv;
    }
}
