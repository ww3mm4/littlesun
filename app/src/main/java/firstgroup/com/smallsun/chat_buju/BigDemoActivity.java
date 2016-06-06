package firstgroup.com.smallsun.chat_buju;

import android.app.Activity;
import android.os.Bundle;

import firstgroup.com.smallsun.R;

public class BigDemoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_big);

		getActionBar().hide();
		CoverManager.getInstance().init(this);

		CoverManager.getInstance().setMaxDragDistance(350);
		CoverManager.getInstance().setExplosionTime(150);
	}

}
