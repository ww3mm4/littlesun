package firstgroup.com.smallsun.qunactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.MyAdapter;
import imsdk.data.IMMyself;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.group.IMGroupInfo;
import imsdk.data.group.IMMyselfGroup;
import imsdk.data.group.IMSDKGroup;
import imsdk.data.mainphoto.IMSDKMainPhoto;
import imsdk.data.relations.IMMyselfRelations;

/**
 * Created by Cherrys on 2015/11/19.
 */
public class TirenActivity extends AppCompatActivity {
    private ListView lv;
    private MyAdapter<String> adapter;
    private String groupId;
    private boolean isMyGroup;
    private  ArrayList<String> groupMemberCustomUserIDsList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yaoqing_activity);
        groupId = getIntent().getStringExtra("groupId");
        lv = (ListView) findViewById(R.id.q_allHaoyou);

        setActionBar(groupId);  //设置自定义actionBar
        isMyGroup = IMMyselfGroup.isMyOwnGroup(groupId);

        IMGroupInfo groupInfo = IMSDKGroup.getGroupInfo(groupId);
        groupMemberCustomUserIDsList = groupInfo.getMemberList();

        init_adapter();
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isMyGroup){
                    // 4. 移除群成员
                    IMMyselfGroup.removeMember(groupMemberCustomUserIDsList.get(position), groupId, new IMMyself.OnActionListener() {
                        @Override
                        public void onSuccess() {
                            // 移除成功回调
                            Toast.makeText(TirenActivity.this,"踢人成功",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onFailure(String error) {
                            // 移除失败回调
                            Toast.makeText(TirenActivity.this,"踢人失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(TirenActivity.this,"对不起，你没有群主权限",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setActionBar(String groupId) {
        View view = getLayoutInflater().inflate(R.layout.qun_title, null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);

        ImageView iv_back = (ImageView) view.findViewById(R.id.c_q_titleBack);
        TextView tvTitle = (TextView) view.findViewById(R.id.c_q_tvTitle);

        tvTitle.setText(IMSDKGroup.getGroupInfo(groupId).getGroupName());
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
