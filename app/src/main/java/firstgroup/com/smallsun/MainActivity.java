package firstgroup.com.smallsun;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import firstgroup.com.data.ZhangEventData;
import firstgroup.com.service.DangLiaoService;
import firstgroup.com.service.GroupMemberService;
import firstgroup.com.service.NetworkStateService;
import firstgroup.com.service.RelationService;
import firstgroup.com.smallsun.adapter.main_ViewPagerAdapter;
import firstgroup.com.smallsun.fragment.CommuFragment;
import firstgroup.com.smallsun.fragment.ContactFragment;
import firstgroup.com.smallsun.fragment.PersonFragment;
import firstgroup.com.smallsun.fragment.ShequFragment;
import firstgroup.com.smallsun.huactivity.Act_Leida;
import firstgroup.com.smallsun.huactivity.Act_SaomiaoEWM;
import firstgroup.com.smallsun.huactivity.Act_ShengchengEWM;
import firstgroup.com.smallsun.huactivity.Act_WeiZhi;
import firstgroup.com.smallsun.huactivity.IMConfiguration;
import firstgroup.com.smallsun.utils.NetWorkUtils;
import firstgroup.com.smallsun.zidingyiview.BitmapView;
import firstgroup.com.viewdonghua.ZoomOutPageTransformer;
import imsdk.data.IMMyself;
import imsdk.data.custommessage.IMMyselfCustomMessage;
import imsdk.data.relations.IMMyselfRelations;
import imsdk.views.IMEmotionTextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private TabLayout mtabLayout;
    private ViewPager mViewPager;
    private main_ViewPagerAdapter adapter;
    private List<Fragment> fragment_list;
    private final String POSITION = "position";
    private  ActionBar actionBar;
    private PopupWindow popup;
    private RelativeLayout rLayout1, rLayout2, rLayout3, rLayout4, rLayout5;
    private BitmapView leida,erweima,dingwei,erweima3;
    private View view;
    private DrawerLayout id_main;
    private DrawerLayout mDrawerLayout;
    private AlertDialog albumDialog;
    private AlertDialog albumDialog_kaifazhe;
    private static SoundPool mSoundPool;
    private static Vibrator mNotificationVibrator;
    private static int mNotificationID;
    private static int mMessageID;
    private Intent groupService;
    private RelativeLayout re_bag;
    private Intent intent_RelationService;
    private  Intent intent_NetworkStateService;

    private Intent intent_DangliaoService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        setTitle("SmallSun");
        initView();

        initView2();
        initEvents();

        fragment_list = new ArrayList<Fragment>();
        fragment_list.add(new CommuFragment());
        fragment_list.add(new ContactFragment());
        fragment_list.add(new ShequFragment());
        fragment_list.add(new PersonFragment());
        adapter = new main_ViewPagerAdapter(getSupportFragmentManager(), fragment_list, this);
        mViewPager.setAdapter(adapter);

        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mtabLayout.setTabsFromPagerAdapter(adapter);
        mtabLayout.setupWithViewPager(mViewPager);
        mtabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mtabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mtabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        setActionBar();
        add_haoyou();

        startGroupService();  //开启群组聊天的监听
        startRelationService();
        startDangliaoService();
        startNetworkStateService();
        Recervehuidiao();
        ZidingyiHuidiao();
        startDangliaoService();
    }
    private void startNetworkStateService(){
        intent_NetworkStateService = new Intent(this, NetworkStateService.class);
        startService(intent_NetworkStateService);
    }
    public void onEventMainThread(ZhangEventData event) {
        if (event.getWath()==555){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("请设置网络");
            builder.setTitle("提示");
            builder.setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetWorkUtils.openNetworkConfig(MainActivity.this);
                }
            });
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
           Dialog dialog= builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

    }

    private void startRelationService(){
        intent_RelationService = new Intent(MainActivity.this, RelationService.class);
        startService(intent_RelationService);
    }
    private void startDangliaoService(){
        intent_DangliaoService = new Intent(MainActivity.this,DangLiaoService.class);
        startService(intent_DangliaoService);
    }

    private void startGroupService() {
         groupService = new Intent(MainActivity.this, GroupMemberService.class);
        startService(groupService);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.add_haoyou:
                popup.showAtLocation(mViewPager, Gravity.CENTER, 0, 0);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    private void add_haoyou(){
        final EditText et = (EditText) view.findViewById(R.id.add_friends_et);
        final EditText et2 = (EditText) view.findViewById(R.id.add_friends_ms);
        TextView tv = (TextView) view.findViewById(R.id.add_friends_add);
        TextView tv2= (TextView) view.findViewById(R.id.add_friends_quxiao);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String userID = et.getText().toString();
                String ms = et2.getText().toString();
                if (TextUtils.isEmpty(userID)) {
                    et.startAnimation(getAnimation(R.anim.shake));
                } else {
                    IMMyselfRelations.sendFriendRequest(ms, userID, 10, new IMMyself.OnActionListener() {

                        public void onSuccess() {
                            // 发送成功（注意！不是添加成功）
                            Toast.makeText(MainActivity.this,"发送成功",Toast.LENGTH_LONG).show();
                        }

                        public void onFailure(String error) {
                            // 发送失败
                            Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG).show();
                        }

                    });
                    popup.dismiss();
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
    }
    public void setActionBar(){

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

    }





    private void initView() {
        mtabLayout = (TabLayout) findViewById(R.id.main_tabLayout);
        mViewPager = ((ViewPager) findViewById(R.id.main_viewpager));
        view = this.getLayoutInflater().inflate(R.layout.activity_add_friends,null);
        WindowManager wm = (WindowManager) MainActivity.this.getSystemService(this.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        popup = new PopupWindow(view,(int)(width*0.8),(int)(height*0.6),true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        if (mSoundPool == null) {
            mSoundPool = new SoundPool(3, AudioManager.STREAM_SYSTEM, 5);
            mNotificationID = mSoundPool.load(this, R.raw.crystalring, 1);
            mMessageID = mSoundPool.load(this, R.raw.msg, 1);
        }

    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mtabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this
            ).setTitle("退出登录").setMessage("确定要退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopService(groupService);
                    stopService(intent_DangliaoService);
                    stopService(intent_RelationService);
                    stopService( intent_NetworkStateService);
                    IMMyself.logout();
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            }).setNegativeButton("后台运行", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PackageManager pm = getPackageManager();
                    ResolveInfo homeInfo =
                            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                    ActivityInfo ai = homeInfo.activityInfo;
                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
                    startActivity(startIntent);
                }
            }).create().show();
        }
        return true;
    }

    public void OpenRightMenu(View view) {
        mDrawerLayout.openDrawer(Gravity.RIGHT);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,
                Gravity.RIGHT);
    }
    private void initEvents() {
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {
                    float leftScale = 1 - 0.3f * scale;
                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent,
                            mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                } else {
                    ViewHelper.setTranslationX(mContent,
                            -mMenu.getMeasuredWidth() * slideOffset);
                    ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
                    ViewHelper.setPivotY(mContent,
                            mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale



                    );
                    ViewHelper.setScaleY(mContent, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // mDrawerLayout.setDrawerLockMode(
                // DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
            }
        });
    }

    private void initView2() {
        re_bag =(RelativeLayout) findViewById(R.id.Re_bag);
        id_main = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        rLayout1 = (RelativeLayout) findViewById(R.id.RL_left1);
        rLayout2 = (RelativeLayout) findViewById(R.id.RL_left2);
        rLayout3 = (RelativeLayout) findViewById(R.id.RL_left3);
        rLayout4 = (RelativeLayout) findViewById(R.id.RL_left4);
        rLayout5 = (RelativeLayout) findViewById(R.id.RL_left5);
        leida = (BitmapView) findViewById(R.id.iv_main_leida);
        erweima = (BitmapView) findViewById(R.id.iv_main_erweima);
        dingwei = (BitmapView) findViewById(R.id.iv_main_dingwei);
        erweima3 = (BitmapView) findViewById(R.id.iv_main_erweima3);

        leida.setOnClickListener(this);
        erweima.setOnClickListener(this);
        dingwei.setOnClickListener(this);
        erweima3.setOnClickListener(this);
        rLayout1.setOnClickListener(this);
        rLayout2.setOnClickListener(this);
        rLayout3.setOnClickListener(this);
        rLayout4.setOnClickListener(this);
        rLayout5.setOnClickListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
        // mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
        // Gravity.RIGHT);
    }
    /************ 点击事件 ****************************/
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.RL_left1:
                re_bag.setBackgroundResource(R.mipmap.img_1);
                break;

            case R.id.RL_left2:
                re_bag.setBackgroundResource(R.mipmap.img_2);
                break;
            case R.id.RL_left3:
                re_bag.setBackgroundResource(R.mipmap.img_3);
                break;
            case R.id.RL_left4:
                re_bag.setBackgroundResource(R.mipmap.img_4);
                break;

            case R.id.RL_left5:
                re_bag.setBackgroundResource(R.mipmap.img_5);
                break;
            case R.id.iv_main_leida:
                Intent intent = new Intent(MainActivity.this, Act_Leida.class);
                startActivity(intent);
                break;
            case R.id.iv_main_erweima:
                showDialoga();
                break;
            case  R.id.iv_main_dingwei:
                Intent intent_weizhi = new Intent(MainActivity.this, Act_WeiZhi.class);
                startActivity(intent_weizhi);

                break;
            case  R.id.iv_main_erweima3:
                showDialoga_kaifazhe();

                break;
        }
    }
    private Animation getAnimation(int id){
        return AnimationUtils.loadAnimation(this, id);
    }


    private void  Recervehuidiao(){

        // 设置接收消息回调
        IMMyself.setOnReceiveTextListener(new IMMyself.OnReceiveTextListener() {
            @Override
            public void onReceiveText(String text, String fromCustomUserID,
                                      long serverActionTime) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast_layout,
                        (ViewGroup) findViewById(R.id.toast_root));
                View view = layout.findViewById(R.id.toast_textview);

                if (view instanceof ImageView) {
                    return;
                }

                if (!(view instanceof IMEmotionTextView)) {
                    return;
                }

                IMEmotionTextView textView = (IMEmotionTextView) layout
                        .findViewById(R.id.toast_textview);

                textView.setGifEmotionText(fromCustomUserID + "于"
                        + getTimeBylong(serverActionTime * 1000) + " 发来消息:" + "\n"
                        + text);

                Toast toast = new Toast(MainActivity.this);

                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                playNotification(true);
            }

            @Override
            public void onReceiveSystemText(String text, long serverActionTime) {
                Toast.makeText(MainActivity.this,
                        getTimeBylong(serverActionTime * 1000) + " 收到系统消息:" + text,
                        Toast.LENGTH_LONG).show();

                playNotification(true);
            }
        });


    }
    private void  ZidingyiHuidiao(){


        IMMyselfCustomMessage
                .setOnReceiveCustomMessageListener(new IMMyselfCustomMessage.OnReceiveCustomMessageListener() {
                    @Override
                    public void onReceiveCustomMessage(String customMessage,
                                                       String fromCustomUserID, long serverActionTime) {
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.toast_layout,
                                (ViewGroup) findViewById(R.id.toast_root));
                        IMEmotionTextView textView = (IMEmotionTextView) layout
                                .findViewById(R.id.toast_textview);

                        textView.setStaticEmotionText(fromCustomUserID + "于"
                                + getTimeBylong(serverActionTime * 1000) + " 发来自定义消息:"
                                + customMessage);

                        Toast toast = new Toast(MainActivity.this);

                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 300);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        playNotification(true);
                    }
                });
    }

    public String getTimeBylong(long longtime) {
        Date date = new Date(longtime);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        String currentTime = format.format(date);

        return currentTime;
    }

    private void showDialoga() {
        albumDialog = new AlertDialog.Builder(MainActivity.this)
                .create();
        albumDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.dialog_usericon, null);
        albumDialog.show();
        albumDialog.setContentView(view);

        ImageView createrweima = (ImageView) view.findViewById(R.id.album_pic);
        createrweima.setImageResource(R.drawable.erweima4);
        TextView tv_shengcheng = (TextView) view.findViewById(R.id.tv_shengcheng);
        tv_shengcheng.setVisibility(View.VISIBLE);

        final ImageView saomiaoerweima = (ImageView) view.findViewById(R.id.camera_pic);
        saomiaoerweima.setImageResource(R.drawable.erweima5);
        TextView tv_saomiao = (TextView) view.findViewById(R.id.tv_saomiao);
        tv_saomiao.setVisibility(View.VISIBLE);

        createrweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDialog.dismiss();
               Intent intent_shengcheng = new Intent(MainActivity.this, Act_ShengchengEWM.class);
                startActivity(intent_shengcheng);
            }
        });
        saomiaoerweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDialog.dismiss();
                Intent intent_saomiao = new Intent(MainActivity.this, Act_SaomiaoEWM.class);
                startActivity(intent_saomiao);

            }
        });
    }



    private void showDialoga_kaifazhe() {
        albumDialog_kaifazhe = new AlertDialog.Builder(MainActivity.this)
                .create();
        albumDialog_kaifazhe.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.tuandui, null);
        albumDialog_kaifazhe.show();
        albumDialog_kaifazhe.setContentView(view);
    }
    // 新消息提醒 - 包括声音提醒、振动提醒
    public static void playNotification(boolean isMessage) {
        if (IMConfiguration.sSoundNotice) {
            if (isMessage) {
                mSoundPool.play(mMessageID, 1, 1, 0, 0, 1);
            } else {
                mSoundPool.play(mNotificationID, 1, 1, 0, 0, 1);
            }
        }

        if (IMConfiguration.sVibrateNotice) {
            mNotificationVibrator.vibrate(200);
        }
    }


}
