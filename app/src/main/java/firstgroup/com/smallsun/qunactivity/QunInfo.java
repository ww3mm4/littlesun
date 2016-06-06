package firstgroup.com.smallsun.qunactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import firstgroup.com.smallsun.R;
import imsdk.data.IMMyself;
import imsdk.data.group.IMMyselfGroup;
import imsdk.data.group.IMSDKGroup;

/**
 * Created by Cherrys on 2015/11/17.
 */
public class QunInfo extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName,tvInfo;
    private Button btnAddMember,btnCheckMember;
    private String mGroupId;
    private ImageView iv_back;
    private TextView tvTitle;

    private boolean isMyGroup;
    private  String groupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_qun_info);


        setActionBar();  //设置自定义actionBar


        mGroupId = getIntent().getStringExtra("groupId");
       groupName = IMSDKGroup.getGroupInfo(mGroupId).getGroupName();
        tvTitle.setText(groupName);


        initView();

        tvName.setText(groupName);
        String groupInfo = IMSDKGroup.getGroupInfo(mGroupId).getCustomGroupInfo();
        tvInfo.setText(groupInfo);

        isMyGroup = IMMyselfGroup.isMyOwnGroup(mGroupId);

    }

    private void setActionBar() {
        View view = getLayoutInflater().inflate(R.layout.qun_title,null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);

        iv_back = (ImageView) view.findViewById(R.id.c_q_titleBack);
        tvTitle = (TextView) view.findViewById(R.id.c_q_tvTitle);

        iv_back.setOnClickListener(this);
    }

    private void initView() {
        tvName = (TextView) findViewById(R.id.c_q_tvName);
        tvInfo = (TextView) findViewById(R.id.c_q_TvInfo);
        btnAddMember = (Button) findViewById(R.id.c_q_btnAddMenber);
        btnCheckMember = (Button) findViewById(R.id.c_q_btnCheckMember);
        ((Button) findViewById(R.id.c_q_btnQuitGroup)).setOnClickListener(this);
        ((Button) findViewById(R.id.c_q_btnRemoveMember)).setOnClickListener(this);

        btnAddMember.setOnClickListener(this);
        btnCheckMember.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.c_q_btnAddMenber:  //添加群成员
                addMember();
                break;
            case R.id.c_q_btnCheckMember:  //查看群成员
                checkMember();
                break;
            case R.id.c_q_titleBack:   //返回聊天界面
                finish();
                break;
            case R.id.c_q_btnRemoveMember:  //踢人
                Intent intent = new Intent(QunInfo.this,TirenActivity.class);
                intent.putExtra("groupId",mGroupId);
                startActivity(intent);
                break;
            case R.id.c_q_btnQuitGroup:  //解散或退群

                if (isMyGroup){
                    jiesanGroup();
                }else{
                    quitGroup();  //退群
                }
                break;
        }
    }

    private void jiesanGroup() {
        new AlertDialog.Builder(this).setTitle("通知").setMessage("确定要解散该群吗？").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 2. 解散群
                IMMyselfGroup.deleteGroup(mGroupId, new IMMyself.OnActionListener() {
                    @Override
                    public void onSuccess() {
                        showToast(groupName,2);
                    }

                    @Override
                    public void onFailure(String s) {
                        showToast(s,-1);
                    }
                });
                Intent intent = new Intent(QunInfo.this,AllQunListActivity.class);
                startActivity(intent);

                finish();
            }
        }).create().show();
    }

    private void quitGroup() {
        new AlertDialog.Builder(this).setTitle("通知").setMessage("确定要退出该群吗？").setNegativeButton("取消",null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 3. 退群
                IMMyselfGroup.quitGroup(mGroupId, new IMMyself.OnActionListener() {
                    @Override
                    public void onSuccess() {
                        showToast(groupName,0);
                    }

                    @Override
                    public void onFailure(String s) {
                        showToast(s,-1);
                    }
                });
                Intent intent = new Intent(QunInfo.this,AllQunListActivity.class);
                startActivity(intent);

                finish();
            }
        }).create().show();
    }

    private void showToast(String message,int state) {
        String result = "";
        switch (state){
            case 0:  //退群成功
                result = "退出"+message+"成功";
                break;
            case -1:
                result = message;
                break;
            case 2:
                result = "解散"+message+"成功";
                break;
            case 3:
                result = "移除"+message+"成功";
                break;
            case 4:
                result = message+"已被你拉去群";
                break;
        }
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
    }

    /**
     * 查看群成员
     */
    private void checkMember() {
        Intent intent = new Intent(QunInfo.this,AllMemberActivity.class);
        intent.putExtra("groupId", mGroupId);
        startActivity(intent);
    }

    /**
     * 添加群成员方法
     */
    private void addMember() {
        Intent intent = new Intent(QunInfo.this,YaoqingActivity.class);
        intent.putExtra("groupId", mGroupId);
        startActivity(intent);
    }
}
