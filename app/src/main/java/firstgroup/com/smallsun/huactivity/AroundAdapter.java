package firstgroup.com.smallsun.huactivity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import firstgroup.com.smallsun.R;
import imsdk.data.around.IMPositionUtil;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.mainphoto.IMSDKMainPhoto;
import imsdk.views.RoundedImageView;

/**
 * 
 * 周边用户adapter
 *
 */
public class AroundAdapter extends BaseAdapter{

	private List<String> mCidList;
	private Context mContext;
	private double mLatitude;
	private double mLongitude;
	
	public AroundAdapter(Context context, List<String> customUserIDList, 
			double latitude, double longitude) {
		mContext = context;
		mCidList = customUserIDList;
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	@Override
	public int getCount() {
		return mCidList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCidList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder mHolder;
		if (convertView == null){
			convertView = View.inflate(mContext, R.layout.item_around,null);
			mHolder = new ViewHolder();
			mHolder.aroundHead = (RoundedImageView)convertView.findViewById(R.id.around_head);
			mHolder.aroundName = (TextView)convertView.findViewById(R.id.around_name);
			mHolder.aroundTime = (TextView)convertView.findViewById(R.id.around_time);
			mHolder.aroundmessage = (TextView)convertView.findViewById(R.id.around_message);

			
			convertView.setTag(mHolder);
		}else{
			
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		IMUser mUser = IMSDKCustomUserInfo.getIMUser(mCidList.get(position));
		if(mUser != null){
			//昵称，没有则显示cid
			if(mUser.getNickname() != null && mUser.getNickname().length() > 0){
				mHolder.aroundName.setText(mUser.getNickname()+"");	
			}else{
				mHolder.aroundName.setText(mUser.getCustomUserID()+"");
			}

			//心情
			String iiiid=getItem(position).toString();
			String customUserInfo = IMSDKCustomUserInfo.get(iiiid);
			if (customUserInfo!=null&&customUserInfo.length()>0)
			mHolder.aroundmessage.setText(customUserInfo);
				//String customUserInfo = IMSDKCustomUserInfo.get(getItem(position));
			//头像

				Glide.with(mContext).load(IMSDKMainPhoto.getLocalUri(mUser.getCustomUserID())).asBitmap().placeholder(R.drawable.login_logo).into(mHolder.aroundHead);

			
			//两者间距离
			//当前用户gps与list用户gps间的距离
			if(mLatitude != 0 && 
					mLongitude != 0 && 
					mUser.getLatitude() != 0 && 
					mUser.getLongitude() != 0){
				
				//坐标系必须是WGS84，保留两位小米，单位:米
				double disVal = IMPositionUtil.getDistanceByWGS84(mLatitude, mLongitude,
						mUser.getLatitude(), mUser.getLongitude());
				
				mHolder.aroundTime.setText("距离"+disVal+"米");
			}else{
				mHolder.aroundTime.setText("");
			}
		}
		
		
		return convertView;
	}
	
	public void setGPS(double latitude, double longitude){
		mLatitude = latitude;
		mLongitude = longitude;
	}
	
	public class ViewHolder
	{
		public RoundedImageView aroundHead;
		public TextView aroundName;
		public TextView aroundTime;
		public  TextView aroundmessage;
	}

}
