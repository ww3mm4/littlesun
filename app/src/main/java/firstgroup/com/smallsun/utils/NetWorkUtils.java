package firstgroup.com.smallsun.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mugua on 2015/11/19.
 */
public class NetWorkUtils {
    public static boolean isNetWorkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context

                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (manager != null) {

            NetworkInfo info = manager.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {

                return true;

            } else {

                return false;

            }

        } else {

            return false;

        }

    }
    public static void openNetworkConfig(Activity activity)
    {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        activity.startActivityForResult(intent, 0);
        intent = null;
        activity = null;
    }
    public static void openNetworkConfig(Service service)
    {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        service.startActivity(intent);
        intent = null;
        service = null;
    }
}
