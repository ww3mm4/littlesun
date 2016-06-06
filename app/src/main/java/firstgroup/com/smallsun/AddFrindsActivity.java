package firstgroup.com.smallsun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import imsdk.data.IMMyself;
import imsdk.data.relations.IMMyselfRelations;

public class AddFrindsActivity extends AppCompatActivity {
    private TextView queren;
    private  String fromCustomUserID;
    private   String text;
    private  TextView jujie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_frinds);

        setActionBar();  //设置自定义actionBar

        queren = (TextView) findViewById(R.id.add_friends_queren);
        jujie = (TextView) findViewById(R.id.add_friends_jujue);
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        fromCustomUserID = intent.getStringExtra("fromCustomUserID");

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

        tvTitle.setText("好友申请");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void init(){
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMMyselfRelations.agreeToFriendRequest(fromCustomUserID, 8, new IMMyself.OnActionListener() {

                    @Override

                    public void onSuccess() {
                        // 回执发送成功
                        Toast.makeText(AddFrindsActivity.this,"添加好友成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override

                    public void onFailure(String error) {
                        // 回执发送失败
                        Toast.makeText(AddFrindsActivity.this,error,Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
        jujie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMMyselfRelations.rejectToFriendRequest(IMMyself.getCustomUserID(), fromCustomUserID, 8,
                        new IMMyself.OnActionListener() {

                            @Override
                            public void onSuccess() {
                                // TODO Auto-generated method stub
                                Toast.makeText(AddFrindsActivity.this,"拒接成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(String arg0) {
                                // TODO Auto-generated method stub
                                Toast.makeText(AddFrindsActivity.this,arg0,Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
