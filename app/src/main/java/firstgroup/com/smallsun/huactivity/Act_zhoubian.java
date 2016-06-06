package firstgroup.com.smallsun.huactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import java.util.ArrayList;

import firstgroup.com.smallsun.R;
import imsdk.data.IMMyself;
import imsdk.data.around.IMGps;
import imsdk.data.around.IMMyselfAround;
import imsdk.data.around.IMMyselfAround.OnAroundActionListener;
import imsdk.data.around.IMPositionUtil;
import imsdk.data.relations.IMMyselfRelations;

/**
 * Created by 胡加宏 on 2015/11/17.
 */
// 周边用户列表界面
// 主要演示功能：
// 1. 获取周边用户列表
// 2. 刷新周边用户列表
public class Act_zhoubian extends Activity implements OnItemClickListener,
        PullToRefreshListView.OnRefreshListener, OnItemLongClickListener, PullToRefreshListView.OnLoadListener {

    //高德定位参数（可选）
    private AMapLocationListener mListener;
    private LocationManagerProxy mLocationManagerProxy;
    //gps
    private double mLatitude = 0;
    private double mLongitude = 0;

    //list adapter
    protected AroundAdapter mAdapter;
    protected TextView mEmptyTextView;
    protected PullToRefreshListView mListView;
    protected ProgressBar mInitProgressBar;
    private ImageButton iv_lleft;
    private TextView biaoti;
    private View view;
    private PopupWindow popup;
    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //layout
       setContentView(R.layout.act_zhoubian);
biaoti = (TextView) findViewById(R.id.mid);
        biaoti.setText("周围的小伙伴");
        //获取上一次定位的gps
        Location mlocation = IMMyselfAround.getLastKnownLocation();
        if(mlocation != null){
            mLatitude = mlocation.getLatitude();
            mLongitude = mlocation.getLongitude();
        }

        //list adapter
        mInitProgressBar = (ProgressBar)this.findViewById(R.id.init_progressbar);
        mInitProgressBar.setVisibility(View.GONE);


        mAdapter = new AroundAdapter(Act_zhoubian.this, IMMyselfAround.getAllUsers(), mLatitude, mLongitude);

        mEmptyTextView = (TextView) findViewById(R.id.empty_textview);
        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setAdapter(mAdapter);
        mListView.setEmptyView(mEmptyTextView);

        mListView.showHeadFirstTime();
        mListView.setPullEnable(true);
        mListView.setLoadEnable(true);//可以加载更多
iv_lleft = (ImageButton) findViewById(R.id.left);
        iv_lleft.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setOnLoadListener(this);

        // 设置标题栏显示内容
        setTitle("周围用户");

        //方法一：使用imsdk接口定位
    initIMSDKLocation();

        //方法二：使用高德定位（可选）
     //   initAMAPLocation();
        view = this.getLayoutInflater().inflate(R.layout.activity_add_friends,null);
        WindowManager wm = (WindowManager) this.getSystemService(this.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        popup = new PopupWindow(view,(int)(width*0.8),(int)(height*0.6),true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        add_haoyou();
    }

    /**
     * 初始化高德定位
     */
    private void add_haoyou(){
         et = (EditText) view.findViewById(R.id.add_friends_et);
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
                            Toast.makeText(Act_zhoubian.this, "发送成功", Toast.LENGTH_LONG).show();
                        }

                        public void onFailure(String error) {
                            // 发送失败
                            Toast.makeText(Act_zhoubian.this, error, Toast.LENGTH_LONG).show();
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
    private Animation getAnimation(int id){
        return AnimationUtils.loadAnimation(this, id);
    }
    private void initAMAPLocation(){
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);

        mListener = new AMapLocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){

                    //高德的火星(gcj02)坐标系 转成 WGS84坐标系
                    IMGps mGps = IMPositionUtil.gcj02ToGps84(amapLocation.getLatitude(),
                            amapLocation.getLongitude());
                    mLatitude = mGps.getLatitude();
                    mLongitude = mGps.getLongitude();

                    Log.d("imsdk", "===定位结果\ngetLatitude:" + mLatitude + "\ngetLongitude:" + mLongitude);

                    // 刷新周边用户列表
                    IMMyselfAround.update(mLatitude, mLongitude, new OnAroundActionListener() {
                        @Override
                        public void onSuccess(ArrayList customUserIDsListInCurrentPage) {
                            //更新gps
                            mAdapter.setGPS(mLatitude, mLongitude);
                            // 刷新列表
                            mAdapter.notifyDataSetChanged();

                            // 停止下拉刷新动画
                            mListView.onRefreshComplete();

                            Log.d("imsdk", "===更新周边成功:"+customUserIDsListInCurrentPage.toArray());
                        }

                        @Override
                        public void onFailure(String error) {
                            // 停止下拉刷新动画
                            mListView.onRefreshComplete();

                            // 提示加载失败
                            Toast.makeText(Act_zhoubian.this, "加载失败:" + error, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            }
        };
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, mListener);
        mLocationManagerProxy.setGpsEnable(false);
    }


    private void initIMSDKLocation() {
        // 刷新周边用户列表
        IMMyselfAround.update(new OnAroundActionListener() {
            @Override
            public void onSuccess(ArrayList customUserIDsListInCurrentPage) {
                // 刷新列表
                mAdapter.notifyDataSetChanged();

                // 停止下拉刷新动画
                mListView.onRefreshComplete();
            }

            @Override
            public void onFailure(String error) {
                // 停止下拉刷新动画
                mListView.onRefreshComplete();

                // 提示加载失败
                Toast.makeText(Act_zhoubian.this, "加载失败:" + error, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //停止定位
        deactivate();
    }

    /**
     * 停止定位
     */
    public void deactivate() {
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(mListener);
            mLocationManagerProxy.destroy();
        }
        mLocationManagerProxy = null;
        mListener = null;
        Log.d("imsdk", "===定位 destroy");
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        popup.showAtLocation(mEmptyTextView, Gravity.CENTER, 0, 0);
        et.setText(IMMyselfAround.getUser(position - 1));

    }

    @Override
    public void onRefresh() {
        initIMSDKLocation();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
        return false;
    }

    @Override
    public void onLoad() {

        Toast.makeText(Act_zhoubian.this, "加载更多...", Toast.LENGTH_SHORT).show();

        IMMyselfAround.nextPage(new OnAroundActionListener() {

            @Override
            public void onSuccess(ArrayList customUserIDsListInCurrentPage) {

                // 停止刷新动画
                mListView.onLoadComplete();
                mAdapter.notifyDataSetChanged();

                Toast.makeText(Act_zhoubian.this, "加载完成", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                // 停止刷新动画
                mListView.onLoadComplete();

                if(error.contains("Already Reach The End")){
                    //没有更多了
                    Toast.makeText(Act_zhoubian.this, "没有更多了", Toast.LENGTH_SHORT)
                            .show();
                }else{
                    // 提示加载失败
                    Toast.makeText(Act_zhoubian.this, "加载失败:" + error, Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });

    }
}