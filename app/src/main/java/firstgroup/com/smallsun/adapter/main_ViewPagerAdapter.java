package firstgroup.com.smallsun.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import firstgroup.com.smallsun.R;

/**
 * Created by Cherrys on 2015/11/15.
 */
public class main_ViewPagerAdapter extends FragmentPagerAdapter {
    private String[] titles = {"聊天","联系人","社区","我的"};
    private List<Fragment> fragmentList;
    private int[] imageRes = {R.drawable.main_1,R.drawable.main_5,R.drawable.main_6,R.drawable.main_3};
    private Context context;

    public main_ViewPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,Context context) {
        super(fm);
        this.fragmentList = fragmentList;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList!=null?fragmentList.get(position):null;
    }

    @Override
    public int getCount() {
        return fragmentList!=null?fragmentList.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
    public View getTabView(int position){
        View view = LayoutInflater.from(context).inflate(R.layout.tab_item, null);
        TextView tv= (TextView) view.findViewById(R.id.tab_item_text);
        tv.setText(titles[position]);
        ImageView img = (ImageView) view.findViewById(R.id.tab_item_image);
        img.setImageResource(imageRes[position]);
        return view;
    }
}
