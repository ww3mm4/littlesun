package firstgroup.com.cehua;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MyApplication2 extends Application {
	// 1.建立一个请求队列，并设置他为全局的。
	private static RequestQueue mQueue;

	@Override
	public void onCreate() {
		super.onCreate();
		mQueue = Volley.newRequestQueue(getApplicationContext());
	}

	public static RequestQueue getHttpQueue() {
		return mQueue;
	}
}
