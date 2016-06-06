package firstgroup.com.smallsun.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.S_AllTopicAdapter;
import firstgroup.com.smallsun.shequ.CreateTopic;
import firstgroup.com.view.SelfListView;
import imsdk.data.IMMyself;
import imsdk.data.community.CMTopicInfo;
import imsdk.data.community.IMCommunity;
import imsdk.data.mainphoto.IMMyselfMainPhoto;

public class ShequFragment extends Fragment implements View.OnClickListener {
    private SelfListView allTop_list;

    private View view;
    private final int recordNums = 20;

    private List<CMTopicInfo> all_Topic = new ArrayList<CMTopicInfo>();
    private Activity activity1;

    private S_AllTopicAdapter topicAdapter;

    private ScrollView s_scrollView;
    private boolean isLoading = false;

    private CircleImageView ivTouxiang;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.activity1 = context;
    }

    @Override
    public void onResume() {
        getAllTopic();
        topicAdapter.notifyDataSetChanged();
        super.onResume();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main_shequfragment, null);
        initView(view);

        topicAdapter = new S_AllTopicAdapter(all_Topic,activity1);
        allTop_list.setAdapter(topicAdapter);

        getAllTopic();

        registerForContextMenu(allTop_list);
        return view;

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(3, 1, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        if (item.getItemId()==1&&item.getGroupId()==3){

            IMCommunity.deleteTopic(all_Topic.get(menuInfo.position).getTopicId(),
                    new IMMyself.OnActionListener() {

                        @Override
                        public void onSuccess() {
                            //成功
                            Toast.makeText(getActivity(),"删除成功",Toast.LENGTH_SHORT).show();
                            getAllTopic();
                            topicAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(getActivity(),"你不能删除别人的说说哦！！！！",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        return super.onContextItemSelected(item);
    }

    private void getAllTopic() {
        IMCommunity.getNewFriendsTopicList(recordNums, new IMCommunity.OnGetDynamicTopicsListener() {
            @Override
            public void onSuccess(List list, boolean b) {
                List<CMTopicInfo> topicInfoList = list;
                boolean mIsover = b;

                if (topicInfoList == null || topicInfoList.size() == 0) {
                    Toast.makeText(activity1, "没有话题信息", Toast.LENGTH_SHORT).show();
                } else {
                    //显示所有话题
                    all_Topic.clear();
                    all_Topic.addAll(topicInfoList);

                    topicAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(activity1, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        ((Button) view.findViewById(R.id.s_btnCreate)).setOnClickListener(this);
        allTop_list = ((SelfListView) view.findViewById(R.id.s_allTop));
        s_scrollView = (ScrollView) view.findViewById(R.id.s_scrollView);
        s_scrollView.setOnTouchListener(new TouchListenerImpl());

        ivTouxiang = (CircleImageView) view.findViewById(R.id.s_userIv);

        // 2. 本地获取本用户的头像 Bitmap
        Bitmap myBitmap = IMMyselfMainPhoto.get(80, 80);
        if (myBitmap != null) {
            ivTouxiang.setImageBitmap(myBitmap);
        }
    }

    /**
     * scrollView 的滑动监听
     */
    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY=view.getScrollY();
                    int height=view.getHeight();
                    int scrollViewMeasuredHeight=s_scrollView.getChildAt(0).getMeasuredHeight();
                    if((scrollY+height)==scrollViewMeasuredHeight){
                        //滑动到了底部
                        if (!isLoading){
                            //加载下一页
                            isLoading = true;
                            loadNextTopic();
                        }

                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    }

    private void loadNextTopic() {
        IMCommunity.getNextFriendsTopicList(recordNums, new IMCommunity.OnGetDynamicTopicsListener() {
            @Override
            public void onSuccess(List list, boolean b) {
                List<CMTopicInfo> topicInfoList = list;
                boolean mIsover = b;

                if (topicInfoList == null || topicInfoList.size() == 0) {
                    Toast.makeText(activity1, "没有话题信息", Toast.LENGTH_SHORT).show();
                } else {
                    //显示所有话题
                    long preTime = all_Topic.get(all_Topic.size()-1).getTopicTime();
                    if (topicInfoList.get(0).getTopicTime()<preTime){
                        all_Topic.addAll(topicInfoList);
                        topicAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(activity1, s, Toast.LENGTH_SHORT).show();
            }
        });
        isLoading = false;
    }

    ;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.s_btnCreate:
                startActivity(new Intent(getActivity(), CreateTopic.class));
                break;
        }
    }
}