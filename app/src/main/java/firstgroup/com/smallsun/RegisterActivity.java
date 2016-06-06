package firstgroup.com.smallsun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.android.percent.support.PercentLinearLayout;

import firstgroup.com.service.RelationService;
import firstgroup.com.smallsun.huactivity.CreateQRImageTest;
import firstgroup.com.smallsun.utils.NetWorkUtils;
import firstgroup.com.view.CustomDialog;
import imsdk.data.IMMyself;

public class RegisterActivity extends Activity {
    private EditText username;
    private EditText passward;
    private EditText passward2;
    private ImageView username_iv;
    private ImageView passward_iv;
    private ImageView passward2_iv;
    private TextView regist;
    private TextView back;
    private Dialog dialog;
    private PercentLinearLayout register_linear;
    private PercentLinearLayout register_result;
    private TextView register_result_name;
    private TextView register_result_passward;
    private TextView register_result_time;
    private TextView register_backloading;
    private String nameString;
    private String passwardString;
    private ImageView regist_layout_iv;
    private TextView register_biaoti;
    private ImageView register_2vma;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init_view();
        init_Listen();

    }
    private void init_view(){
        username = (EditText) findViewById(R.id.register_usename);
        passward = (EditText) findViewById(R.id.register_usepassward);
        passward2 = (EditText) findViewById(R.id.register_usepassward2);
        regist = (TextView) findViewById(R.id.regist_regist);
        username_iv = (ImageView) findViewById(R.id.register_usename_iv);
        passward_iv  = (ImageView) findViewById(R.id.register_usepassward_iv);
        passward2_iv = (ImageView) findViewById(R.id.register_usepassward_iv2);
        back = (TextView) findViewById(R.id.regist_back);
        register_linear = (PercentLinearLayout) findViewById(R.id.register_linear);
        register_result = (PercentLinearLayout) findViewById(R.id.register_result);
        register_result_name = (TextView) findViewById(R.id.register_result_name);
        register_result_passward = (TextView) findViewById(R.id.register_result_passward);
        register_result_time = (TextView) findViewById(R.id.register_result_time);
        register_backloading = (TextView) findViewById(R.id.regist_backloading);
        regist_layout_iv = (ImageView) findViewById(R.id.regist_layout_iv);
        register_biaoti = (TextView) findViewById(R.id.register_biaoti);
        register_2vma = (ImageView) findViewById(R.id.register_result_2vma);
        Animation ani = new AlphaAnimation(0f, 1f);
        ani.setDuration(1500);
        ani.setRepeatMode(Animation.REVERSE);
        ani.setRepeatCount(Animation.INFINITE);
        register_biaoti.startAnimation(ani);
     }

    private void init_Listen() {
        regist_layout_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_translate_top,
                        R.anim.out_translate_top);
                finish();
            }
        });
        register_backloading.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                intent.putExtra("name",nameString);
                intent.putExtra("passward", passwardString);
                startActivity(intent);
                overridePendingTransition(R.anim.in_translate_top,
                        R.anim.out_translate_top);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_translate_top,
                        R.anim.out_translate_top);
                finish();
            }
        });
        regist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    username_iv.setVisibility(View.VISIBLE);
                } else {
                    username_iv.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        passward.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    passward_iv.setVisibility(View.VISIBLE);
                } else {
                    passward_iv.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        passward2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() > 0) {
                    passward2_iv.setVisibility(View.VISIBLE);
                } else {
                    passward2_iv.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });

        username_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                username.setText("");
            }
        });
        passward_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                passward.setText("");

            }
        });
        passward2_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                passward2.setText("");
            }
        });
    }

    private void register() {
        nameString = username.getText().toString();
        passwardString = passward.getText().toString();
        final String passString2 = passward2.getText().toString();
        if (TextUtils.isEmpty( nameString)) {
            username.startAnimation(getAnimation(R.anim.shake));
        } else if (TextUtils.isEmpty(passwardString)) {
            passward.startAnimation(getAnimation(R.anim.shake));
        } else if (TextUtils.isEmpty(passString2)) {
            passward2.startAnimation(getAnimation(R.anim.shake));
        } else if (!passwardString.equals(passString2)) {
            passward.startAnimation(getAnimation(R.anim.shake));
            passward2.startAnimation(getAnimation(R.anim.shake));
        }else if(!NetWorkUtils.isNetWorkAvailable(RegisterActivity.this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setMessage("请设置网络");
            builder.setTitle("提示");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetWorkUtils.openNetworkConfig(RegisterActivity.this);
                }
            });
            builder.setNegativeButton("取消",null);
            builder.create().show();
        }
        else {
            dialog = loadingDialog(RegisterActivity.this);
            // 步骤1. 设置登录账号customUserID
            IMMyself.setCustomUserID(nameString);
            // 步骤2. 设置登录密码
            IMMyself.setPassword(passwardString);
            // 步骤3.调用注册接口
            IMMyself.register(5, new IMMyself.OnActionListener() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    register_linear.setVisibility(View.GONE);
                    register_result.setVisibility(View.VISIBLE);
                    register_backloading.setVisibility(View.VISIBLE);
                    register_result_name.setText("您的用户名是：" + nameString);
                    register_result_passward.setText("您的密码是：" + passwardString);
                    register_result_time.setText("注册时间是：" + getTime());
                    CreateQRImageTest createQRImageTest = new CreateQRImageTest(register_2vma);
                    createQRImageTest.createQRImage(nameString);
                    Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this,R.anim.slide_down_in);
                    animation.setDuration(1000);
                    animation.setFillAfter(true);
                    register_result.startAnimation(animation);
                }

                @Override
                public void onFailure(String error) {
                    dialog.dismiss();
                    if (error.equals("Timeout")) {
                        error = "注册超时";
                        register_linear.startAnimation(getAnimation(R.anim.shake));
                    }

                    Toast.makeText(RegisterActivity.this, "注册失败：" + error,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //跟具id获取补帧动画
    private Animation getAnimation(int id){
        return AnimationUtils.loadAnimation(this, id);
    }

    private Dialog loadingDialog(Context context) {
        CustomDialog customDialog = new CustomDialog(context, R.layout.dialog_layout,
                R.style.DialogTheme);
        customDialog.show();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        return customDialog;
    }
    private String getTime() {
        String str = DateUtils.formatDateTime(this,
                System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                        | DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_ABBREV_ALL);
        return str;
    }
}
