package firstgroup.com.smallsun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import firstgroup.com.data.ZhangEventData;
import firstgroup.com.myglide.GlideCircleTransform;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.MyAdapter;
import firstgroup.com.smallsun.huactivity.Act_ChatShow;
import firstgroup.com.smallsun.huactivity.ImageDisplayer;
import firstgroup.com.smallsun.utils.CustomAsyTask;
import imsdk.data.IMMyself;
import imsdk.data.IMSDK;
import imsdk.data.customuserinfo.IMSDKCustomUserInfo;
import imsdk.data.customuserinfo.IMUser;
import imsdk.data.mainphoto.IMSDKMainPhoto;
import imsdk.data.relations.IMMyselfRelations;

/**
 * Created by mugua on 2015/11/17.
 */
public class HaoYouListFragment extends Fragment {
    private List<String> list = new ArrayList<String>();
    private MyAdapter<String> adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = new ListView(getActivity());
        listView.setBackgroundResource(R.drawable.editshape01);
        init_adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Act_ChatShow.class);
                intent.putExtra("CustomUserID", list.get(position));
                startActivity(intent);
            }
        });

        IMMyselfRelations.setOnDataChangedListener(new IMSDK.OnDataChangedListener() {
            @Override
            public void onDataChanged() {
                updata();
            }

        });
        registerForContextMenu(listView);
        onUpdata();
        return listView;
    }
    private void onUpdata(){
        if(IMMyselfRelations.isInitialized()){
            updata();
        }else {
            IMMyselfRelations.setOnInitializedListener(new IMMyself.OnInitializedListener() {
                @Override
                public void onInitialized() {
                    updata();
                }
            });
        }
    }

    private void updata(){
        list.clear();
        ArrayList<String> customUserIDsList = IMMyselfRelations.getFriendsList();
        list.addAll(customUserIDsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 11, 0, "删除");
        menu.add(0,12,0,"拉黑");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = item.getItemId();
        if (id==11){
            IMMyselfRelations.removeUserFromFriendsList(list.get(menuInfo.position), 8, new IMMyself.OnActionListener() {

                @Override

                public void onSuccess() {

                    // 移除执行成功（注意！移除和添加不一样，移除不需要对方授权）
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                }


                @Override

                public void onFailure(String error) {

                    // 移除失败
                    Toast.makeText(getActivity(), "移除失败", Toast.LENGTH_SHORT).show();
                }

            });


        }else if(id==12){
            IMMyselfRelations.moveUserToBlacklist(list.get(menuInfo.position), 8, new IMMyself.OnActionListener() {

                @Override

                public void onSuccess() {

                    // 拉黑成功
                    EventBus.getDefault().post(new ZhangEventData(0x123));
                    Toast.makeText(getActivity(), "拉黑成功", Toast.LENGTH_SHORT).show();
                }


                @Override

                public void onFailure(String error) {

                    // 拉黑失败
                    Toast.makeText(getActivity(), "拉黑失败", Toast.LENGTH_SHORT).show();
                }

            });

        }

        return super.onContextItemSelected(item);
    }

    private void init_adapter(){
        adapter = new MyAdapter<String>(list, R.layout.list_item,getActivity()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Holder holder = new Holder();
                if (convertView==null){
                    holder = new Holder();
                    convertView = getItemView();
                    holder.iv = (ImageView) convertView.findViewById(R.id.list_item_iv);
                    holder.ms = (TextView) convertView.findViewById(R.id.list_item_ms);
                    holder.name = (TextView) convertView.findViewById(R.id.list_item_name);
                    convertView.setTag(holder);
                }else {
                    holder = (Holder) convertView.getTag();
                }
                IMUser mUser = IMSDKCustomUserInfo.getIMUser(getItem(position));
                if(mUser.getNickname() != null && mUser.getNickname().length() > 0){
                    holder.name.setText(mUser.getNickname() + "");
                }else{
                    holder.name.setText(mUser.getCustomUserID()+"");
                }
                if(mUser.getMainPhotoFileID() != null && mUser.getMainPhotoFileID().length() > 0){
                    Glide.with(getActivity()).load(IMSDKMainPhoto.getLocalUri(mUser.getCustomUserID())).asBitmap().placeholder(R.drawable.login_logo).into(holder.iv);
                }

                String customUserInfo = IMSDKCustomUserInfo.get(getItem(position));
                holder.ms.setText(customUserInfo);
                return convertView;
            }
            class Holder{
                TextView name;
                TextView ms;
                ImageView iv;
            }
        };
    }
}
