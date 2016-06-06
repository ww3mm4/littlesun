package firstgroup.com.smallsun.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.huactivity.ImageDisplayer;
import firstgroup.com.smallsun.shequ.AllPinglunByIdActivity;
import firstgroup.com.smallsun.utils.CustomAsyTask;
import imsdk.data.IMMyself;
import imsdk.data.community.CMTopicInfo;
import imsdk.data.community.IMCommunity;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.mainphoto.IMSDKMainPhoto;

/**
 * Created by Cherrys on 2015/11/18.
 */
public class S_AllTopicAdapter extends BaseAdapter {
    private List<CMTopicInfo> data;
    private Context context;
    public S_AllTopicAdapter(List<CMTopicInfo> data,Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.s_topic_item,null);
            vh.ivTouxiang = ((CircleImageView) convertView.findViewById(R.id.s_topicIv));
            vh.tvContent = ((TextView) convertView.findViewById(R.id.s_topicContent));
            vh.tvName = (TextView) convertView.findViewById(R.id.s_topicTv);
            vh.tvTime = (TextView) convertView.findViewById(R.id.s_topicTime);
            vh.tvCai = (TextView) convertView.findViewById(R.id.s_tvCai);
            vh.tvPinglun = (TextView) convertView.findViewById(R.id.s_tvPinglun);
            vh.tvTupian = (TextView) convertView.findViewById(R.id.s_tvTupian);
            vh.tvZan = (TextView) convertView.findViewById(R.id.s_tvZan);
            vh.ivZan = (ImageView) convertView.findViewById(R.id.s_ivZan);
            vh.ivPinglun = (ImageView) convertView.findViewById(R.id.s_ivPinglun);

            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final CMTopicInfo info =  data.get(position);
        vh.tvName.setText(info.getTopicCreator().getCustomUserID());
        vh.tvContent.setText(info.getTopicContent());
        long time = info.getTopicTime()*1000;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        vh.tvTime.setText(df.format(new Date(time)));
        //头像的显示
        IMUser user = IMSDKCustomUserInfo.getIMUser(info.getTopicCreator().getCustomUserID());
        Glide.with(context).load(IMSDKMainPhoto.getLocalUri(info.getTopicCreator().getCustomUserID())).asBitmap().placeholder(R.drawable.login_logo).into(vh.ivTouxiang);
        //评论什么的显示
        vh.tvZan.setText(info.getPraiseNums()+"赞");
        vh.tvCai.setText(info.getDespiseNums() + "踩");
        vh.tvPinglun.setText(info.getCommentNums() + "评论");
        if (info.getTopicImages()!=null) {
            vh.tvTupian.setText(info.getTopicImages().length + "张图片,点击查看");
        }
        vh.tvPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nums = data.get(position).getCommentNums();
                if (nums==0){
                    Toast.makeText(context,"赶紧去抢沙发吧！",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(context, AllPinglunByIdActivity.class);
                    intent.putExtra("topicId",data.get(position).getTopicId());
                    context.startActivity(intent);
                }
            }
        });
        vh.tvTupian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTupian(info);
            }
        });
        vh.ivPinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinglun(position);
            }
        });
        vh.ivZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zan(position);
            }


        });
        return convertView;
    }
    private void zan(int position) {
        IMCommunity.likeTopic(data.get(position).getTopicId(),
                new IMMyself.OnActionListener() {
                    @Override
                    public void onSuccess() {
                        //成功
                        Toast.makeText(context,"点赞成功",Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(context,"您已经赞过了哦！",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void pinglun(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("评论");
        final EditText etContent = new EditText(context);
        etContent.setHint("尽情的吐槽吧！");
        builder.setMessage("请在下面进行评论");
        builder.setView(etContent).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String content = etContent.getText().toString().trim();
                if ("".equals(content)) content = " ";
                IMCommunity.createComment(data.get(position).getTopicId(), content,
                        new IMMyself.OnActionResultListener() {
                            @Override
                            public void onSuccess(Object result) {
                                Toast.makeText(context,"评论成功",Toast.LENGTH_SHORT).show();
                                //创建成功
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(context,"评论失败",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).setNegativeButton("取消",null).create().show();
    }

    /**
     * 显示图片
     */
    private void showTupian(CMTopicInfo info) {
        if (info.getTopicImages()!=null){
            View view = LayoutInflater.from(context).inflate(R.layout.show_tupian,null);
            GridView gv = (GridView) view.findViewById(R.id.t_show);
            tianchong(gv,info);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("显示话题图片");
            builder.setView(view);
            builder.setPositiveButton("关闭",null).create().show();
        }else{
            Toast.makeText(context,"这个话题没有图片哦",Toast.LENGTH_SHORT).show();
        }
    }

    private void tianchong(GridView gv,CMTopicInfo info) {
        if (info.getTopicImages()!=null) {
            String[] data = info.getTopicImages();
            ShowTupianAdapter adapter = new ShowTupianAdapter(data, context);
            gv.setAdapter(adapter);
        }
    }

    class ViewHolder{
        private CircleImageView ivTouxiang;
        private TextView tvName,tvContent,tvTime,tvZan,tvCai,tvPinglun,tvTupian;
        private ImageView ivZan,ivPinglun;
    }

}
