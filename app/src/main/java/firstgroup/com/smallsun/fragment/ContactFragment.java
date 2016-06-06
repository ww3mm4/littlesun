package firstgroup.com.smallsun.fragment;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import firstgroup.com.adapter.MyViewPagerAdapet;
import firstgroup.com.data.ZhangEventData;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.qunactivity.AllQunListActivity;
import firstgroup.com.viewdonghua.ZoomOutPageTransformer;
import imsdk.data.IMMyself;
import imsdk.data.IMSDK;
import imsdk.data.relations.IMMyselfRelations;

/**
 * Created by Cherrys on 2015/11/15.
 */
public class ContactFragment extends Fragment implements View.OnClickListener {
    @Nullable
    private Button btn_qunliao;
    private TextView haoyou;
    private TextView heimingdang;
    private List<TextView> textViews = new ArrayList<TextView>();
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_contactfragment,null);
        initView(view);
        setViewPageradapter();
        return view;
    }

    private void initView(View view) {
        btn_qunliao = (Button) view.findViewById(R.id.contact_BtnQunliao);
        btn_qunliao.setOnClickListener(this);
        haoyou = (TextView) view.findViewById(R.id.contact_haoyou_tv);
        heimingdang = (TextView) view.findViewById(R.id.contact_heimingdan_tv);
        viewPager = (ViewPager) view.findViewById(R.id.contact_heimingdan_vp);
        haoyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haoyou.setBackground(getResources().getDrawable(R.drawable.list_shape2));
                haoyou.setTextColor(getResources().getColor(R.color.baise));
                heimingdang.setBackground(getResources().getDrawable(R.drawable.list_shape3));
                heimingdang.setTextColor(getResources().getColor(R.color.text_orang_color));
                viewPager.setCurrentItem(0);

            }
        });
        heimingdang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haoyou.setBackground(getResources().getDrawable(R.drawable.list_shape));
                haoyou.setTextColor(getResources().getColor(R.color.text_orang_color));
                heimingdang.setBackground(getResources().getDrawable(R.drawable.list_shape4));
                heimingdang.setTextColor(getResources().getColor(R.color.baise));
                viewPager.setCurrentItem(1);

            }
        });

    }
    private void setViewPageradapter(){
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new HaoYouListFragment());
        fragments.add(new HeiMingDangListFragment());
        MyViewPagerAdapet adapet = new MyViewPagerAdapet(getChildFragmentManager(),fragments);
        viewPager.setAdapter(adapet);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    haoyou.setBackground(getResources().getDrawable(R.drawable.list_shape2));
                    haoyou.setTextColor(getResources().getColor(R.color.baise));
                    heimingdang.setBackground(getResources().getDrawable(R.drawable.list_shape3));
                    heimingdang.setTextColor(getResources().getColor(R.color.text_orang_color));
                }
                else if (position==1){
                    haoyou.setBackground(getResources().getDrawable(R.drawable.list_shape));
                    haoyou.setTextColor(getResources().getColor(R.color.text_orang_color));
                    heimingdang.setBackground(getResources().getDrawable(R.drawable.list_shape4));
                    heimingdang.setTextColor(getResources().getColor(R.color.baise));
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.contact_BtnQunliao:
                Intent intent = new Intent(getActivity(), AllQunListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
