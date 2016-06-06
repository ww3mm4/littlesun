package firstgroup.com.smallsun.utils;

import android.content.Context;
import android.widget.Toast;

import imsdk.data.IMMyself;

/**
 * Created by Cherrys on 2015/11/17.
 */
public class ActionListenerUtils implements IMMyself.OnActionListener {
    private Context context;
    public ActionListenerUtils(Context context){
        this.context = context;
    }
    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(String s) {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}
