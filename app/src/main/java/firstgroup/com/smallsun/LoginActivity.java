package firstgroup.com.smallsun;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import firstgroup.com.myglide.GlideCircleTransform;
import firstgroup.com.smallsun.utils.NetWorkUtils;
import firstgroup.com.view.CustomDialog;
import firstgroup.com.view.VDHLayout;
import imsdk.data.IMMyself;
import android.os.Handler;
public class LoginActivity extends Activity {

    private ImageView logo;
    private TextView title;
    private EditText username;
    private EditText userpassward;
    private ImageView username_iv;
    private ImageView userpassward_iv;
    private TextView login;
    private TextView register;
    private VDHLayout vdhLayout;
    private Dialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init_view();
        init_anim();
        init();
        init_Listen();
    }

    //初始化控件
    private void init_view(){
        logo = (ImageView) findViewById(R.id.login_logo);
        title = (TextView) findViewById(R.id.login_title);
        username = (EditText) findViewById(R.id.login_usename);
        userpassward = (EditText) findViewById(R.id.login_usepassward);
        login = (TextView) findViewById(R.id.login_login);
        register = (TextView) findViewById(R.id.login_register);
        userpassward_iv = (ImageView) findViewById(R.id.login_usepassward_iv);
        username_iv = (ImageView) findViewById(R.id.login_usename_iv);
        vdhLayout = (VDHLayout) findViewById(R.id.login_vdhlayout);
    }

    //初始化控件
    private void init(){
        Glide.with(this).load(R.drawable.login_logo).transform(new GlideCircleTransform(this, 180)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(logo);
        Intent intent = getIntent();
        String nameString = intent.getStringExtra("name");
        String passwardString = intent.getStringExtra("passward");
        if(!TextUtils.isEmpty(nameString)&&!TextUtils.isEmpty(passwardString)){
            username.setText(nameString);
            userpassward.setText(passwardString);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    login.performClick();
                }
            }, 1500);
        }

    }

    //添加初始化动画
    private void init_anim() {
        title.startAnimation(getAnimation(R.anim.alpha_translate_rotate));
        username.startAnimation(getAnimation(R.anim.username));
        userpassward.startAnimation(getAnimation(R.anim.password));
        login.startAnimation(getAnimation(R.anim.logintv));
        register.startAnimation(getAnimation(R.anim.registertv));
    }

    //跟具id获取补帧动画
    private Animation getAnimation(int id){
        return AnimationUtils.loadAnimation(this,id);
    }

    //添加事件监听
    private void init_Listen() {

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                login();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_translate_top,
                        R.anim.out_translate_top);
                finish();
            }
        });
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    username_iv.setVisibility(View.VISIBLE);
                } else {
                    username_iv.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        userpassward.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.length() > 0) {
                    userpassward_iv.setVisibility(View.VISIBLE);
                } else {
                    userpassward_iv.setVisibility(View.GONE);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        username_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                username.setText("");
            }
        });
        userpassward_iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                userpassward.setText("");
            }
        });
    }

    private void login() {
        final String usenameString = username.getText().toString();
        String passwardString = userpassward.getText().toString();

        if (TextUtils.isEmpty(usenameString)) {
            username.startAnimation(getAnimation(R.anim.shake));

        } else if (TextUtils.isEmpty(passwardString)) {
            userpassward.startAnimation(getAnimation(R.anim.shake));
        }else if(!NetWorkUtils.isNetWorkAvailable(this)){
            username.startAnimation(getAnimation(R.anim.shake));
            userpassward.startAnimation(getAnimation(R.anim.shake));
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setMessage("请设置网络");
            builder.setTitle("提示");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetWorkUtils.openNetworkConfig(LoginActivity.this);
                }
            });
            builder.setNegativeButton("取消",null);
            builder.create().show();
        }
        else {
            dialog = loadingDialog(LoginActivity.this);
            // 登录共3步
            // 步骤1.设置账号customUserID
            IMMyself.setCustomUserID(usenameString);
            // 步骤2.设置登录密码
            IMMyself.setPassword(passwardString);
            // 步骤3.
            IMMyself.login(5, new IMMyself.OnActionListener() {
                @Override
                public void onSuccess() {
                    dialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    dialog.dismiss();
                    if (error.equals("Timeout")) {
                        error = "登录超时";
                        vdhLayout.startAnimation(getAnimation(R.anim.shake));
                    } else if (error.equals("Wrong Password")) {
                        error = "密码错误";
                        userpassward.startAnimation(getAnimation(R.anim.shake));
                    } else if (error.equals("CustomUserID don't Exist")) {
                        error = "无此ID";
                        username.startAnimation(getAnimation(R.anim.shake));
                    }
                    Toast.makeText(LoginActivity.this, error+"", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }
    //生成自定义对话会框
    private Dialog loadingDialog(Context context) {
        CustomDialog customDialog = new CustomDialog(context, R.layout.dialog_layout,
                R.style.DialogTheme);
        customDialog.show();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        return customDialog;
    }
}
