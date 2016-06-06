package firstgroup.com.smallsun.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.MyChatAdapter;
import firstgroup.com.smallsun.chat_buju.CoverManager;
import firstgroup.com.smallsun.chat_buju.WaterDrop;
import firstgroup.com.smallsun.huactivity.Act_ChatShow;
import firstgroup.com.smallsun.huactivity.Constants;
import firstgroup.com.smallsun.huactivity.DBHelper;
import firstgroup.com.smallsun.huactivity.MerchantInfo;
import firstgroup.com.smallsun.hudata.Chat_Data;
import firstgroup.com.smallsun.utils.DateUtil;
import firstgroup.com.smallsun.utils.UserMessage;
import firstgroup.com.smallsun.zidingyiview.BitmapView;
import firstgroup.com.smallsun.zidingyiview.MyListViewForScrollView;
import imsdk.data.IMSDK;
import imsdk.data.localchatmessagehistory.IMChatMessage;
import imsdk.data.localchatmessagehistory.IMMyselfLocalChatMessageHistory;
import imsdk.data.mainphoto.IMSDKMainPhoto;
import imsdk.data.nickname.IMSDKNickname;
import imsdk.data.recentcontacts.IMMyselfRecentContacts;

/**
 * Created by 胡加宏 on 2015/11/15.
 */
public class CommuFragment extends Fragment {
    private View view;
    private MyListViewForScrollView mListView;
    private MyChatAdapter myChatAdapter;
    private List<Chat_Data> list;
    private Activity activity1;

    private TextView messageempty;

    public boolean mShowingGroupMessage;
    // ui
    private BroadcastReceiver mReceiver;
    private MessageListAdapter mAdapter;

    List<String> userLists;
    private List<UserMessage> userMessages;


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        mAdapter = new MessageListAdapter();
        mAdapter.notifyDataSetChanged();
    }

    private void update() {


    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_chat, null);

        initView();

        // 获取所有最近联系人的 CustomUserID
        // 得到CustomUserID列表
        ArrayList<String> customUserIDsList = IMMyselfRecentContacts.getUsersList();
        //  Log.e("ID","--"+customUserIDsList.toString());
        //  Log.e("ID", "--" + customUserIDsList.get(0).toString());
        boolean result = IMMyselfRecentContacts.clearUnreadChatMessage();


        CoverManager.getInstance().init(activity1);


        CoverManager.getInstance().setMaxDragDistance(150);
        CoverManager.getInstance().setExplosionTime(150);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String customUserID = userMessages.get(position).getCustomUserID();
                if (customUserID.startsWith("kefu_")) {
                    //客服服务号时
                    MerchantInfo minfo = DBHelper.queryMerchantInfoByCid(activity1, customUserID);
                    Intent intent = new Intent(activity1, Act_ChatShow.class);
                    intent.putExtra("CustomUserID", customUserID);
                    if (customUserID.equals(Constants.IMDEV_KEFU_SERVICEID)) {
                        intent.putExtra("mName", "爱萌客服");
                    } else if (minfo != null && !TextUtils.isEmpty(minfo.getShop_name())) {
                        intent.putExtra("mName", minfo.getShop_name());
                    } else {
                        intent.putExtra("mName", customUserID);
                    }

                    startActivity(intent);

                } else {
                    //普通账号时
                    Intent intent = new Intent(activity1, Act_ChatShow.class);
                    intent.putExtra("CustomUserID", customUserID);
                    startActivity(intent);
                }

                IMMyselfRecentContacts.clearUnreadChatMessage(customUserID);

                int unreadMessageCount = (int) IMMyselfRecentContacts
                        .getUnreadChatMessageCount();

//                CustomRadioGroup.sSingleton.setItemNewsCount(0,
//                        unreadMessageCount > 0 ? unreadMessageCount : -1);

                initData();
                mAdapter.notifyDataSetChanged();
            }
        });

        IMMyselfRecentContacts.setOnDataChangedListener(new IMSDK.OnDataChangedListener() {
            @Override
            public void onDataChanged() {
                initData();
                mAdapter.notifyDataSetChanged();

                int unreadMessageCount = (int) IMMyselfRecentContacts
                        .getUnreadChatMessageCount();

                // 设置未读消息数字红点提醒
//                CustomRadioGroup.sSingleton.setItemNewsCount(0,
//                        unreadMessageCount > 0 ? unreadMessageCount : -1);
            }
        });

        //消息更新监听
        IntentFilter ift = new IntentFilter();
        ift.addAction(Constants.BROADCAST_ACTION_NOTIFY_MESSAGE);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                initData();
                mAdapter.notifyDataSetChanged();

                int unreadMessageCount = (int) IMMyselfRecentContacts
                        .getUnreadChatMessageCount();
                // 设置未读消息数字红点提醒
//                CustomRadioGroup.sSingleton.setItemNewsCount(0,
//                        unreadMessageCount > 0 ? unreadMessageCount : -1);

            }
        };
        activity1.registerReceiver(mReceiver, ift);

        return view;
    }
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 33, 0, "删除记录");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        int id = item.getItemId();
        if (id == 33) {
          // Log.e("diyge", "---" + userLists.get(0));
            boolean b = IMMyselfRecentContacts.removeUser(userLists.get(menuInfo.position).toString());
         //   Log.e("diyge", "---" + userLists.get(menuInfo.position));
          //  IMMyselfRecentContacts.removeUser("xx");
            if (b){
                    Toast.makeText(activity1,"删除成功", Toast.LENGTH_SHORT).show();}
            else {
                Toast.makeText(activity1, "删除失败", Toast.LENGTH_SHORT).show();}
            }


            initData();
            mAdapter.notifyDataSetChanged();


        return super.onContextItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        mListView.requestDisallowInterceptTouchEvent(true);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity1 = activity;
    }

    private void initView() {

        mListView = (MyListViewForScrollView) view.findViewById(R.id.lv_talk);
        messageempty = (TextView) view.findViewById(R.id.messages_empty_textview);
        mListView.setEmptyView(messageempty);
        registerForContextMenu(mListView);

    }

    public void initData() {
        userMessages = new ArrayList<UserMessage>();
        userLists = IMMyselfRecentContacts.getUsersList();
        UserMessage userMessage = null;
        for (int i = 0; i < userLists.size(); i++) {
            String cid = String.valueOf(userLists.get(i));
            userMessage = new UserMessage();
            userMessage.setCustomUserID(cid);

            IMChatMessage chatMessage = IMMyselfLocalChatMessageHistory
                    .getLastChatMessage(cid);
            if (chatMessage != null) {
                userMessage.setLastMessageContent(chatMessage.getText());
                userMessage.setLastMessageTime(DateUtil
                        .getTimeBylong(
                                (chatMessage.getServerSendTime() == 0 ? chatMessage.getClientSendTime() : chatMessage.getServerSendTime())
                                        * 1000));
                userMessage.setUnreadChatMessageCount(IMMyselfRecentContacts
                        .getUnreadChatMessageCount(cid));
                userMessage.setNickname(IMSDKNickname.get(cid));

                userMessage.setBitmap(IMSDKMainPhoto.get(cid));

                userMessages.add(userMessage);

            }

        }
    }

    private class MessageListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return userMessages.size();
        }

        @Override
        public Object getItem(int position) {
            return userMessages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final UserMessage userMessage = userMessages.get(position);
            ItemViewHolder itemViewHolder = null;

            if (convertView == null) {
                itemViewHolder = new ItemViewHolder();
                convertView = LayoutInflater.from(activity1).inflate(
                        R.layout.item_chat_list, null);
                itemViewHolder.chat_name = (TextView) convertView
                        .findViewById(R.id.tv_chat_name);
                itemViewHolder.photo = (BitmapView) convertView
                        .findViewById(R.id.iv_chat_photo);
                itemViewHolder.chat_content = (TextView) convertView
                        .findViewById(R.id.tv_chat_content);
                itemViewHolder.chat_time = (TextView) convertView
                        .findViewById(R.id.tv_chat_time);
                itemViewHolder.chat_drop = (WaterDrop) convertView
                        .findViewById(R.id.drop);

                convertView.setTag(itemViewHolder);
            } else {
                itemViewHolder = (ItemViewHolder) convertView.getTag();
            }

            // 如果存在新的消息，则设置BadgeView
            if (userMessage.getUnreadChatMessageCount() > 0) {
                itemViewHolder.chat_drop.setVisibility(View.VISIBLE);
                itemViewHolder.chat_drop.setText("" + (int) userMessage.getUnreadChatMessageCount());
            } else {
                if (itemViewHolder.chat_drop != null) {
                    itemViewHolder.chat_drop.setVisibility(View.INVISIBLE);
                }
            }

            Glide.with(getActivity()).load(IMSDKMainPhoto.getLocalUri(userMessage.getCustomUserID())).asBitmap().placeholder(R.drawable.login_logo).into(itemViewHolder.photo);
            String name = userMessage.getNickname();
            if (name != null && name.length() > 0)
                itemViewHolder.chat_name.setText(name);
            else
                itemViewHolder.chat_name.setText(userMessage.getCustomUserID());
            itemViewHolder.chat_time.setText(userMessage.getLastMessageTime());
            itemViewHolder.chat_content.setText(userMessage.getLastMessageContent());


            return convertView;

        }


        private final class ItemViewHolder {
            BitmapView photo;
            TextView chat_name, chat_content, chat_time;
            WaterDrop chat_drop;
        }
    }
}

