package firstgroup.com.smallsun.qunactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.huactivity.CreateQRImageTest;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.group.IMGroupInfo;
import imsdk.data.mainphoto.IMSDKMainPhoto;


public class PersonalinformationActivity extends AppCompatActivity {
    private String id;
    private ImageView pr_iv;
    private TextView pr_name;
    private TextView pr_ms;
    private ImageView pr_2vm;
    private IMUser mUser;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinformation);
        setActionBar();  //设置自定义actionBar
        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        mUser = IMSDKCustomUserInfo.getIMUser(id);
        init_view();
        init();
    }
    private void setActionBar() {
        View view = getLayoutInflater().inflate(R.layout.qun_title,null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);

        ImageView iv_back = (ImageView) view.findViewById(R.id.c_q_titleBack);
        TextView tvTitle = (TextView) view.findViewById(R.id.c_q_tvTitle);

        tvTitle.setText("个人资料");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void init_view(){
        pr_iv = (ImageView) findViewById(R.id.pf_iv);
        pr_name = (TextView) findViewById(R.id.pf_name);
        pr_ms = (TextView) findViewById(R.id.pf_ms);
        pr_2vm = (ImageView) findViewById(R.id.pf_2vm);
    }
    private void init(){
        Glide.with(this).load(IMSDKMainPhoto.getLocalUri(mUser.getCustomUserID())).asBitmap().placeholder(R.drawable.login_logo).into(pr_iv);
        String name ;
        if (mUser.getNickname()!=null&&mUser.getNickname().length()>0){
            name = mUser.getNickname();
        }else {
            name=mUser.getCustomUserID();
        }
        pr_name.setText(name);
        String customUserInfo = IMSDKCustomUserInfo.get(mUser.getCustomUserID());
        pr_ms.setText(customUserInfo);
        CreateQRImageTest createQRImageTest = new CreateQRImageTest(pr_2vm);
        createQRImageTest.createQRImage(mUser.getCustomUserID());
    }


}
