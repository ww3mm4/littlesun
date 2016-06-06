package firstgroup.com.smallsun.qunactivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import firstgroup.com.smallsun.MainActivity;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.MyAdapter;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.group.IMGroupInfo;
import imsdk.data.group.IMSDKGroup;
import imsdk.data.mainphoto.IMSDKMainPhoto;

/**
 * Created by Cherrys on 2015/11/19.
 */
public class AllMemberActivity extends AppCompatActivity {
    private ListView lv;
    private MyAdapter<String> adapter;
    private  ArrayList<String> groupMemberCustomUserIDsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_member_activity);
        String groupId = getIntent().getStringExtra("groupId");
        IMGroupInfo groupInfo = IMSDKGroup.getGroupInfo(groupId);

        setActionBar(groupInfo);  //设置自定义actionBar

        lv = (ListView) findViewById(R.id.q_all_member);

        groupMemberCustomUserIDsList = groupInfo.getMemberList();

        init_adapter();
        lv.setAdapter(adapter);
    }
    private void setActionBar(IMGroupInfo groupInfo) {
        View view = getLayoutInflater().inflate(R.layout.qun_title,null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);

        ImageView iv_back = (ImageView) view.findViewById(R.id.c_q_titleBack);
        TextView tvTitle = (TextView) view.findViewById(R.id.c_q_tvTitle);

        tvTitle.setText(groupInfo.getGroupName());
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }
    private void init_adapter() {
        adapter = new MyAdapter<String>(groupMemberCustomUserIDsList, R.layout.list_item, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder = new Holder();
                if (convertView == null) {
                    holder = new Holder();
                    convertView = getItemView();
                    holder.iv = (ImageView) convertView.findViewById(R.id.list_item_iv);
                    holder.ms = (TextView) convertView.findViewById(R.id.list_item_ms);
                    holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                    convertView.setTag(holder);
                } else {
                    holder = (Holder) convertView.getTag();
                }
                IMUser mUser = IMSDKCustomUserInfo.getIMUser(getItem(position));
                if (mUser.getNickname() != null && mUser.getNickname().length() > 0) {
                    holder.name.setText(mUser.getNickname() + "");
                } else {
                    holder.name.setText(mUser.getCustomUserID() + "");
                }
                if (mUser.getMainPhotoFileID() != null && mUser.getMainPhotoFileID().length() > 0) {
                    Glide.with(getActivity()).load(IMSDKMainPhoto.getLocalUri(mUser.getCustomUserID())).asBitmap().placeholder(R.drawable.login_logo).into(holder.iv);
                }

                String customUserInfo = IMSDKCustomUserInfo.get(getItem(position));
                holder.ms.setText(customUserInfo);
                return convertView;
            }

            class Holder {
                TextView name;
                TextView ms;
                ImageView iv;
            }
        };
    }
}
