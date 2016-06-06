package firstgroup.com.smallsun.huactivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.Vector;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.erweima.CameraManager;
import firstgroup.com.smallsun.erweima.CaptureActivityHandler;
import firstgroup.com.smallsun.erweima.InactivityTimer;
import firstgroup.com.smallsun.erweima.ViewfinderView;
import imsdk.data.IMMyself;
import imsdk.data.relations.IMMyselfRelations;

public class Act_SaomiaoEWM extends Activity implements Callback
{

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private PopupWindow popup;
	private View view;
	private FrameLayout xxx;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_saomiaoerweima);
		// 初始化 CameraManager
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		xxx = (FrameLayout) findViewById(R.id.act_saomiao);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		view = this.getLayoutInflater().inflate(R.layout.activity_add_friends,null);
		WindowManager wm = (WindowManager) Act_SaomiaoEWM.this.getSystemService(this.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		popup = new PopupWindow(view,(int)(width*0.8),(int)(height*0.6),true);
		popup.setBackgroundDrawable(new BitmapDrawable());
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface)
		{
			initCamera(surfaceHolder);
		}
		else
		{
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
		{
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		if (handler != null)
		{
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy()
	{
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder)
	{
		try
		{
			CameraManager.get().openDriver(surfaceHolder);
		}
		catch (IOException ioe)
		{
			return;
		}
		catch (RuntimeException e)
		{
			return;
		}
		if (handler == null)
		{
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		if (!hasSurface)
		{
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView()
	{
		return viewfinderView;
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void drawViewfinder()
	{
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(final Result obj, Bitmap barcode)
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		if (barcode == null)
		{
			dialog.setIcon(null);
		}
		else {
			if (obj.getText().toString().length() < 10) {
				popup.showAtLocation(xxx, Gravity.CENTER, 0, 0);
				final EditText et = (EditText) view.findViewById(R.id.add_friends_et);
				final EditText et2 = (EditText) view.findViewById(R.id.add_friends_ms);
				TextView tv = (TextView) view.findViewById(R.id.add_friends_add);
				TextView tv2 = (TextView) view.findViewById(R.id.add_friends_quxiao);
				et.setText(obj.getText().toString());
				et2.setText("我是" + IMMyself.getCustomUserID());

				tv.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						String userID = et.getText().toString();
						String ms = et2.getText().toString();
						if (TextUtils.isEmpty(userID)) {
							et.startAnimation(getAnimation(R.anim.shake));
						} else {
							IMMyselfRelations.sendFriendRequest(ms, userID, 10, new IMMyself.OnActionListener() {

								public void onSuccess() {
									// 发送成功（注意！不是添加成功）
									Toast.makeText(Act_SaomiaoEWM.this, "发送成功", Toast.LENGTH_LONG).show();
								}

								public void onFailure(String error) {
									// 发送失败
									Toast.makeText(Act_SaomiaoEWM.this, error, Toast.LENGTH_LONG).show();
								}

							});
							popup.dismiss();
						}
					}
				});
				tv2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						popup.dismiss();
					}
				});



			}
			else {

				Drawable drawable = new BitmapDrawable(barcode);
				dialog.setIcon(drawable);

			}
		}
if (obj.getText().toString().length()>=10){

	dialog.setTitle("非小太阳用户，普通扫描");
	dialog.setMessage(obj.getText());
	Log.i("tag", "二维码扫描结果：" + obj.getText());
	dialog.setNegativeButton("确定", new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			//用默认浏览器打开扫描得到的地址
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(obj.getText());
			intent.setData(content_url);
			startActivity(intent);
			finish();
		}
	});
	dialog.setPositiveButton("取消", new DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			finish();
		}
	});
	dialog.create().show();
}
	}

	private void initBeepSound()
	{
		if (playBeep && mediaPlayer == null)
		{
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try
			{
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			}
			catch (IOException e)
			{
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate()
	{
		if (playBeep && mediaPlayer != null)
		{
			mediaPlayer.start();
		}
		if (vibrate)
		{
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}
	private Animation getAnimation(int id){
		return AnimationUtils.loadAnimation(this, id);
	}
	private void add_haoyou(){
		final EditText et = (EditText) findViewById(R.id.add_friends_et);
		final EditText et2 = (EditText) findViewById(R.id.add_friends_ms);
		TextView tv = (TextView) findViewById(R.id.add_friends_add);
		TextView tv2= (TextView) findViewById(R.id.add_friends_quxiao);
		tv.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userID = et.getText().toString();
				String ms = et2.getText().toString();
				if (TextUtils.isEmpty(userID)) {
					et.startAnimation(getAnimation(R.anim.shake));
				} else {
					IMMyselfRelations.sendFriendRequest(ms, userID, 10, new IMMyself.OnActionListener() {

						public void onSuccess() {
							// 发送成功（注意！不是添加成功）
							Toast.makeText(Act_SaomiaoEWM.this, "发送成功", Toast.LENGTH_LONG).show();
						}

						public void onFailure(String error) {
							// 发送失败
							Toast.makeText(Act_SaomiaoEWM.this, error, Toast.LENGTH_LONG).show();
						}

					});
					popup.dismiss();
				}
			}
		});
		tv2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});
	}
	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener()
	{
		public void onCompletion(MediaPlayer mediaPlayer)
		{
			mediaPlayer.seekTo(0);
		}
	};

}