package firstgroup.com.smallsun.zidingyiview;

/**
 * Created by 胡加宏 on 2015/11/16.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
//放在listView中的
public class MyListViewForScrollView extends ListView {
    public MyListViewForScrollView(Context context) {
        super(context);
    }
    public MyListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyListViewForScrollView(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}