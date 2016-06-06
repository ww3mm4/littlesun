package firstgroup.com.smallsun.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Cherrys on 2015/11/19.
 */
public class SoundUtils {

    public static boolean getIsCloseSound(Context context){
        SharedPreferences sp = context.getSharedPreferences("sound",Context.MODE_PRIVATE);
        boolean isClose = sp.getBoolean("isClose",false);
        return isClose;
    }
}
