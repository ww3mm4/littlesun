package firstgroup.com.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.widget.Toast;

import firstgroup.com.smallsun.AddFrindsActivity;
import firstgroup.com.smallsun.MainActivity;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.utils.SoundUtils;
import imsdk.data.IMMyself;
import imsdk.data.IMSDK;
import imsdk.data.relations.IMMyselfRelations;

/**
 * Created by mugua on 2015/11/16.
 */
public class RelationService extends Service{
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private Notification notification;
    private NotificationManager manager;



    @Override
    public void onCreate() {
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        haoyou();
    }

    private void haoyou(){
        IMMyselfRelations.setOnRelationsEventListener(new IMMyselfRelations.OnRelationsEventListener() {

            @Override

            public void onInitialized() {

            }


            @Override

            public void onReceiveFriendRequest(String text, String fromCustomUserID, long serverSendTime) {
                //
                // 1. 收到好友请求
                Intent intent = new Intent(RelationService.this,AddFrindsActivity.class);
                intent.putExtra("text",text);
                intent.putExtra("fromCustomUserID",fromCustomUserID);
                PendingIntent contextIntent = PendingIntent.getActivity(RelationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                notification = new Notification.Builder(RelationService.this)
                        .setSmallIcon(R.drawable.login_logo)
                        .setContentText(fromCustomUserID + "对你说" + text)
                        .setContentTitle("您收到一条来自" + fromCustomUserID + "的好友请求！！")
                        .setContentIntent(contextIntent)
                        .setTicker("您收到一条来自" + fromCustomUserID + "的好友请求！！")
                        .build();
                notification.flags=Notification.FLAG_AUTO_CANCEL;
                if (!SoundUtils.getIsCloseSound(RelationService.this)) {
                    notification.defaults = Notification.DEFAULT_SOUND;
                }
                manager.notify(fromCustomUserID.hashCode(), notification);

            }


            @Override

            public void onReceiveAgreeToFriendRequest(String fromCustomUserID, long serverSendTime) {

                // 2. 收到好友请求回执

                // 2.1. 对方已同意
                Toast.makeText(RelationService.this," 对方已同意",Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onReceiveRejectToFriendRequest(String reason, String fromCustomUserID, long serverSendTime) {

                // 2. 收到好友请求回执

                // 2.2. 对方已拒绝
                Toast.makeText(RelationService.this," 对方已拒绝",Toast.LENGTH_SHORT).show();

            }


            @Override

            public void onBuildFriendshipWithUser(String customUserID, long serverSendTime) {

                // 3. 已和某用户建立好友关系
                Toast.makeText(RelationService.this," 已和某用户建立好友关系",Toast.LENGTH_SHORT).show();

            }

        });
    }

}
