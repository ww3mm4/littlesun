package firstgroup.com.smallsun.qunactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import firstgroup.com.smallsun.MainActivity;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.Qun_ListAdapter;
import firstgroup.com.smallsun.utils.ActionListenerUtils;
import imsdk.data.IMMyself;
import imsdk.data.group.IMGroupInfo;
import imsdk.data.group.IMMyselfGroup;
import imsdk.data.group.IMSDKGroup;

/**
 * Created by Cherrys on 2015/11/15.
 */
public class AllQunListActivity extends AppCompatActivity  {
    private ListView qun_list;
    private ArrayList<String> allGroupName = new ArrayList<String>();  //所有群名称
    private Qun_ListAdapter adapter;
    private ArrayList<String> groupIDsList;

    private ArrayList<String> allGroupInfo = new ArrayList<String>();

    private ImageView iv_back;
    private TextView tvTitle;
    @Override
    protected void onResume() {
        super.onResume();
        getAllGroup();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.qun_list_activity);

        setActionBar();  //设置自定义actionBar

        qun_list = (ListView) findViewById(R.id.c_qun_list);



        init();   //群模块初始化
        adapter = new Qun_ListAdapter(allGroupName,this,allGroupInfo);
        qun_list.setAdapter(adapter);

        getAllGroup();   //获得所有的群名称


        //点击群列表项进入群聊天界面
        qun_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent(AllQunListActivity.this, QunLiaotian.class);
                intent.putExtra("groupId", groupIDsList.get(position));
                startActivity(intent);
            }
        });


    }
    private void setActionBar() {
        View view = getLayoutInflater().inflate(R.layout.qun_title,null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);

        iv_back = (ImageView) view.findViewById(R.id.c_q_titleBack);
        tvTitle = (TextView) view.findViewById(R.id.c_q_tvTitle);

        tvTitle.setText("我的群聊列表");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllQunListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    /**
     * 获取所有的群名称
     */
    private void getAllGroup() {
        allGroupName.clear();
        // 1. 获取我的群列表（groupIDsList）
        groupIDsList = IMMyselfGroup.getMyGroupsList();
            for (int i = 0; i < groupIDsList.size(); i++) {
                // 2. 获取指定groupID群的群名称和自定义资料
                IMGroupInfo groupInfo = IMSDKGroup.getGroupInfo(groupIDsList.get(i));
                allGroupName.add(groupInfo.getGroupName());
                allGroupInfo.add(groupInfo.getCustomGroupInfo());
            }
        adapter.notifyDataSetChanged();
    }
    public void addGroup(View v){
        View view = LayoutInflater.from(this).inflate(R.layout.add_group,null);
        final EditText et_name = (EditText) view.findViewById(R.id.c_g_AddnameEt);
        final EditText et_info = (EditText) view.findViewById(R.id.c_g_AddInfoEt);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("创建群").setView(view).setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = et_name.getText().toString().trim();
                final String info = et_info.getText().toString().trim();
                if ("".equals(name)) {
                    Toast.makeText(AllQunListActivity.this, "请输入群名称", Toast.LENGTH_SHORT);
                } else {
                    // 1. 创建群
                    IMMyselfGroup.createGroup(name, new IMMyself.OnActionResultListener() {
                        @Override
                        public void onSuccess(Object result) {
                            if (!(result instanceof String)) {
                                return;
                            }
                            // 成功创建的群都会返回唯一的groupID，类型为String
                            String groupID = (String) result;
                            IMGroupInfo groupInfo = IMSDKGroup.getGroupInfo(groupID);
                            groupInfo.setCustomGroupInfo(info);
                            groupInfo.commitGroupInfo(new ActionListenerUtils(AllQunListActivity.this));
                            getAllGroup();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String error) {
                            // 创建群失败
                            Toast.makeText(AllQunListActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).create().show();
    }
    private void init() {
        // 1. 判断模块是否已初始化
        boolean isInitialized = IMMyselfGroup.isInitialized();
        if (isInitialized){
        }else {
            //2. 监听初始化事件
            IMMyselfGroup.setOnInitializedListener(new IMMyself.OnInitializedListener() {
                @Override
                public void onInitialized() {
                    // 初始化完成的回调
                    getAllGroup();   //获取所有的群名称'
                }
            });
        }
    }
}
