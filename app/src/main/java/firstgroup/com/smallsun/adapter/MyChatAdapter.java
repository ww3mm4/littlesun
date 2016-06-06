package firstgroup.com.smallsun.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.chat_buju.DropCover;
import firstgroup.com.smallsun.chat_buju.WaterDrop;
import firstgroup.com.smallsun.hudata.Chat_Data;

/**
 * Created by 胡加宏 on 2015/11/15.
 */
public class MyChatAdapter extends MyBaseAdapter {
    private List<Chat_Data> list;
private Context context;

    public MyChatAdapter(List list, Context context, int layoutResId) {
        super(list, context, layoutResId);
        this.list = list;
        this.context = context;
    }

    @Override
    public void setData(MyViewHolder holder, final int position) {
        TextView tv_name = ((TextView) holder.findView(R.id.tv_chat_name));
        ImageView iv_photo = ((ImageView) holder.findView(R.id.iv_chat_photo));
        final TextView content = ((TextView) holder.findView(R.id.tv_chat_content));
    //    final TextView num = ((TextView) holder.findView(R.id.notify_text));
        tv_name.setText(list.get(position).getName());
        content.setText(list.get(position).getFinleMessage());
//        num.setText(String.valueOf(list.get(position).getFinlecount()));

        WaterDrop drop = (WaterDrop) holder.findView(R.id.drop);
        drop.setText(String.valueOf(position));

        drop.setOnDragCompeteListener(new DropCover.OnDragCompeteListener() {

            public void onDrag() {
               Toast.makeText(context,"消息清除"+position,Toast.LENGTH_SHORT).show();
            }
        });


    }
    }


