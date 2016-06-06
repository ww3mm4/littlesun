package firstgroup.com.smallsun.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import firstgroup.com.data.ZhangEventData;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.MyAdapter;
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
public class HeiMingDangListFragment extends Fragment {
    private List<String> list = new ArrayList<String>();
    private MyAdapter<String> adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        listView = new ListView(getActivity());
        listView.setBackgroundResource(R.drawable.editshape01);
        init_adapter();
        listView.setAdapter(adapter);
        registerForContextMenu(listView);
        return listView;
    }
    public void onEventMainThread(ZhangEventData event) {
        if (event.getWath()==0x123){
            updata();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
        ArrayList<String> customUserIDsList = IMMyselfRelations.getBlacklist();
        list.addAll(customUserIDsList);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(18, 99, Menu.NONE, "移除黑名单");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int id = item.getItemId();
        if (id == 99) {
                IMMyselfRelations.removeUserFromBlacklist(list.get(menuInfo.position), 10, new IMMyself.OnActionListener() {

                    @Override

                    public void onSuccess() {

                        // 移除执行成功
                        onUpdata();
                        Toast.makeText(getActivity(), "移除成功", Toast.LENGTH_SHORT).show();
                    }


                    @Override

                    public void onFailure(String error) {
                        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        // 移除执行失败

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
