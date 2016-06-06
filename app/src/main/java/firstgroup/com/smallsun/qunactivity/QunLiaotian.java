package firstgroup.com.smallsun.qunactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import firstgroup.com.smallsun.R;
import imsdk.views.IMChatView;
import imsdk.views.IMGroupChatView;

/**
 * Created by Cherrys on 2015/11/16.
 */
public class QunLiaotian extends Activity {
    // data
    private String mGroupID;

    // ui
    private IMGroupChatView mGroupChatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupID = getIntent().getStringExtra("groupId");

        // 创建一个IMGroupChatView实例
        mGroupChatView = new IMGroupChatView(this, mGroupID);

        // 添加到当前activity
        setContentView(mGroupChatView);

        // 配置IMGroupChatView实例
        mGroupChatView.setMaxGifCountInMessage(10);
        mGroupChatView.setUserMainPhotoVisible(true);
        mGroupChatView.setTitleBarVisible(true);
        mGroupChatView.setUserMainPhotoCornerRadius(20);
        mGroupChatView.setTitleBarBackground(R.color.text_orang_color);
        mGroupChatView.setChatViewBackground(R.drawable.qun_bg);
        //添加头像点击事件监听
        mGroupChatView.setOnHeadPhotoClickListener(new IMChatView.OnHeadPhotoClickListener() {

            @Override
            public void onClick(View v, String customUserID) {
                Intent intent = new Intent(QunLiaotian.this, PersonalinformationActivity.class);
                intent.putExtra("id",customUserID);
                startActivity(intent);
            }

        });

        mGroupChatView.setRightTitleBarText("群信息");
        mGroupChatView.setOnRightTitleBarClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QunLiaotian.this, QunInfo.class);
                intent.putExtra("groupId", mGroupID);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 为了实现捕获用户选择的图片
        mGroupChatView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 为了实现点击返回键隐藏表情栏onc
        mGroupChatView.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

}