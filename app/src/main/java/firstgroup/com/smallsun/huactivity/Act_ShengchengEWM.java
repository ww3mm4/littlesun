package firstgroup.com.smallsun.huactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import firstgroup.com.smallsun.R;
import imsdk.data.IMMyself;

public class Act_ShengchengEWM extends Activity{

	private TextView tv;
	private ImageView iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_shengchengerweima);
		tv = (TextView)findViewById(R.id.tv);
		iv = (ImageView)findViewById(R.id.iv);
		CreateQRImageTest createQRImageTest = new CreateQRImageTest(iv);
		String sss =IMMyself.getCustomUserID();
		tv.setText(sss);
		createQRImageTest.createQRImage(sss);
	}
	
}
