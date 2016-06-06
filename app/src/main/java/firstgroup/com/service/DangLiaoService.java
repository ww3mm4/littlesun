package firstgroup.com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.huactivity.Act_ChatShow;
import firstgroup.com.smallsun.qunactivity.QunLiaotian;
import firstgroup.com.smallsun.utils.SoundUtils;
import imsdk.data.IMMyself;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.group.IMSDKGroup;
import imsdk.data.mainphoto.IMSDKMainPhoto;

/**
 * Created by mugua on 2015/11/18.
 * 和单聊有关的服务
 */
public class DangLiaoService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        jieshouxiaoxi();
    }
    private void jieshouxiaoxi(){
// 设置监听器
        IMMyself.setOnReceiveTextListener(new IMMyself.OnReceiveTextListener() {

            // 监听来自其他用户的文本讯息

            @Override

            public void onReceiveText(String text, String fromCustomUserID, long serverActionTime) {


                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(DangLiaoService.this);

                IMUser mUser = IMSDKCustomUserInfo.getIMUser(fromCustomUserID);

                Intent intent = new Intent(DangLiaoService.this,Act_ChatShow.class);
                intent.putExtra("CustomUserID", fromCustomUserID);
                PendingIntent contextIntent = PendingIntent.getActivity(DangLiaoService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                String name = "";
                if(mUser.getNickname() != null && mUser.getNickname().length() > 0){
                    name=mUser.getNickname() + "";
                }else{
                    name=mUser.getCustomUserID()+"";
                }

                builder.setContentTitle(name);
                builder.setContentText(name + ":" + text);
                builder.setSmallIcon(R.drawable.touxiang);
                builder.setContentIntent(contextIntent);
                builder.setTicker("smallSun:有您的一条的消息");
                builder.setLargeIcon(IMSDKMainPhoto.get(fromCustomUserID));
                Notification n = builder.build();
                n.flags = Notification.FLAG_AUTO_CANCEL;
                if (!SoundUtils.getIsCloseSound(DangLiaoService.this)) {
                    n.defaults = Notification.DEFAULT_SOUND;
                }
                manager.notify(1000, n);
            }


            // 监听系统消息

            @Override

            public void onReceiveSystemText(String text, long serverActionTime) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(DangLiaoService.this);
                String name = "系统";
                builder.setContentTitle(name);
                builder.setAutoCancel(true);
                builder.setContentText(name + ":" + text);
                builder.setSmallIcon(R.drawable.touxiang);
                builder.setTicker("smallSun:来自系统的消息");
                Notification n = builder.build();
                if (!SoundUtils.getIsCloseSound(DangLiaoService.this)) {
                    n.defaults = Notification.DEFAULT_SOUND;
                }
                manager.notify(0x123, n);
            }

        });
    }
}
