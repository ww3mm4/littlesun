package firstgroup.com.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import java.util.ArrayList;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.qunactivity.QunLiaotian;
import firstgroup.com.smallsun.utils.SoundUtils;
import imsdk.data.IMMyself;
import imsdk.data.group.IMMyselfGroup;
import imsdk.data.group.IMSDKGroup;

/**
 * 接收群资料改变的服务，群成员被移除等
 * Created by Cherrys on 2015/11/17.
 */
public class GroupMemberService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IMMyselfGroup.setOnInitializedListener(new IMMyself.OnInitializedListener() {
            @Override
            public void onInitialized() {
                qunMemberListener();
                qunMessageListener();
            }
        });
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean isInit = IMMyselfGroup.isInitialized();
        if (isInit) {
            qunMemberListener();
            qunMessageListener();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    private void qunMessageListener() {
        // 1. 接收 IM 群消息
        IMMyselfGroup.setOnGroupMessageListener(new IMMyselfGroup.OnGroupMessageListener() {
            @Override
            public void onReceiveText(String s, String s1, String s2, long l) {
                //收到文本消息

                Intent intent = new Intent(GroupMemberService.this,QunLiaotian.class);
                intent.putExtra("groupId",s1);
                PendingIntent contextIntent = PendingIntent.getActivity(GroupMemberService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


                String groupName = IMSDKGroup.getGroupInfo(s1).getGroupName();
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification.Builder builder = new Notification.Builder(GroupMemberService.this);
                builder.setContentTitle(groupName);
                builder.setContentText(s2 + ":" + s);
                builder.setSmallIcon(R.drawable.touxiang);

                builder.setContentIntent(contextIntent);
                builder.setTicker("smallSun:有您的一条新的群消息");
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.touxiang));
                Notification n = builder.build();
                n.flags = Notification.FLAG_AUTO_CANCEL;
                if (!SoundUtils.getIsCloseSound(GroupMemberService.this)) {
                    n.defaults = Notification.DEFAULT_SOUND;
                }
                manager.notify(1, n);

            }

            @Override
            public void onReceiveCustomMessage(String s, String s1, String s2, long l) {
                //收到自定义文本消息
            }

            @Override
            public void onReceiveBitmapMessage(String s, String s1, String s2, long l) {
                //收到图片信息
            }

            @Override
            public void onReceiveBitmap(Bitmap bitmap, String s, String s1, long l) {
                //收到图片
            }

            @Override
            public void onReceiveBitmapProgress(double v, String s, String s1, long l) {
                //接收图片的进度
            }
        });
    }

    private void sendNotification(String info) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(GroupMemberService.this);
        builder.setContentText(info);
        builder.setSmallIcon(R.drawable.touxiang);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.touxiang));
        Notification n = builder.build();
        if (!SoundUtils.getIsCloseSound(GroupMemberService.this)) {
            n.defaults = Notification.DEFAULT_SOUND;
        }
        manager.notify(1, n);
    }

    private void qunMemberListener() {
        // 群事件监听设置
        IMMyselfGroup.setOnGroupEventsListener(new IMMyselfGroup.OnGroupEventsListener() {
            @Override
            public void onInitialized() {
                //群初始化回调的方法
            }

            @Override
            public void onGroupDeletedByUser(String s, String s1, long l) {
                //群被解散的回调
                sendNotification("群被解散");
            }

            @Override
            public void onGroupNameUpdated(String s, String s1, long l) {
                //群名称更新回调
                sendNotification(s1 + "群名已更改为" + s);
            }

            @Override
            public void onCustomGroupInfoUpdated(String s, String s1, long l) {
                //群备注信息更改的回调
            }

            @Override
            public void onGroupMemberUpdated(ArrayList arrayList, String s, long l) {
                //群成员更新的回调

            }

            @Override
            public void onAddedToGroup(String s, long l) {
                //当前用户被拉入群的回调
                sendNotification("您已被拉入" + s + "群");
            }

            @Override
            public void onRemovedFromGroup(String s, String s1, long l) {
                //群成员被移除的回调
                sendNotification(s1 + "已从" + s + "中移除");
            }
        });
    }
}
