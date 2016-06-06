package firstgroup.com.smallsun.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import firstgroup.com.smallsun.LoginActivity;
import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.utils.ImageCompress;
import imsdk.data.IMMyself;
import imsdk.data.IMSDK;
import imsdk.data.customuserinfo.IMMyselfCustomUserInfo;
import imsdk.data.mainphoto.IMMyselfMainPhoto;
import imsdk.data.nickname.IMSDKNickname;

/**
 * Created by Cherrys on 2015/11/15.
 */
public class PersonFragment extends Fragment implements View.OnClickListener {
    @Nullable
    private Button btnLogout;
    private TextView tvName, tvInfo, p_tvProgress;
    private CircleImageView ivTouxiang;

    private String path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DCIM).getAbsolutePath()
            + File.separator + System.currentTimeMillis() + ".jpg";
    private File file;
    private AlertDialog albumDialog;
    private Bitmap myBitmap;
    private Button sundle;
    private Activity activity1;
    private SharedPreferences sp;
    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);
        this.activity1 = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_personfragment, null);

        initView(view);
        init();
        // 2. 本地获取本用户的头像 Bitmap
        Bitmap myBitmap = IMMyselfMainPhoto.get(60, 60);
        if (myBitmap != null) {
            ivTouxiang.setImageBitmap(myBitmap);
        }

        tvName.setText(IMSDKNickname.get());

        // 2. 本地获取本用户的自定义用户资料
        String myCustomUserInfo = IMMyselfCustomUserInfo.get();
        if (myCustomUserInfo != null) {
            tvInfo.setText(myCustomUserInfo);
        }
        sp = getActivity().getSharedPreferences("sound",Context.MODE_PRIVATE);

        return view;
    }

    /**
     * 初始化个人资料模块
     */
    private void init() {
        IMMyselfCustomUserInfo.setOnInitializedListener(new IMMyself.OnInitializedListener() {
            @Override
            public void onInitialized() {
            }
        });
        boolean isPhotoInitialized = IMMyselfMainPhoto.isInitialized();
        IMMyselfMainPhoto.setOnInitializedListener(new IMMyself.OnInitializedListener() {
            @Override
            public void onInitialized() {
                // 头像模块已初始化
            }
        });
        boolean isNameInitialized = IMSDKNickname.isInitialized();
        IMSDKNickname.setOnInitializedListener(new IMMyself.OnInitializedListener() {
            @Override
            public void onInitialized() {

            }
        });
    }

    private void initView(View view) {
        btnLogout = (Button) view.findViewById(R.id.p_btnLogout);
        btnLogout.setOnClickListener(this);
        p_tvProgress = ((TextView) view.findViewById(R.id.p_tvProgress));

        ((Button) view.findViewById(R.id.p_btnSaveChange)).setOnClickListener(this);

        ((Button) view.findViewById(R.id.p_btnChangeName)).setOnClickListener(this);
        sundle = (Button) view.findViewById(R.id.p_btnCloseSound);
        sundle.setOnClickListener(this);

        tvName = ((TextView) view.findViewById(R.id.p_tvName));
        tvInfo = ((TextView) view.findViewById(R.id.p_tvInfo));
        ivTouxiang = ((CircleImageView) view.findViewById(R.id.p_iv));

        ivTouxiang.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.p_btnLogout:
                new AlertDialog.Builder(getActivity()
                ).setTitle("退出登录").setMessage("确定要退出吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IMMyself.logout();
                        Toast.makeText(activity1, "成功退出登录", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                }).setNegativeButton("取消", null).create().show();
                break;
            case R.id.p_iv:
                showDialog();
                break;
            case R.id.p_btnChangeName:
                changeName();
                break;
            case R.id.p_btnSaveChange:   //保存修改
                saveChange();
                break;
            case R.id.p_btnCloseSound:
                closeSound();
                break;
        }
    }

    private void closeSound() {
        boolean b = sp.getBoolean("isClose",true);
        SharedPreferences.Editor editor = sp.edit();
        if (b){
            editor.putBoolean("isClose", false);
            sundle.setText("关闭声音");
        }else {
            editor.putBoolean("isClose",true);
            sundle.setText("开启声音");
        }
        editor.commit();

    }


    /**
     * 保存用户修改的内容
     * 昵称、个性签名、头像
     */
    private void saveChange() {
        IMSDKNickname.commit(tvName.getText().toString().trim(), 10, new IMMyself.OnActionListener() {
            @Override
            public void onSuccess() {
                // 修改昵称成功
                Toast.makeText(activity1, "昵称修改成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                // 修改昵称失败
                Toast.makeText(activity1, error, Toast.LENGTH_SHORT).show();
            }
        });
        IMMyselfCustomUserInfo.commit(tvInfo.getText().toString(), new IMMyself.OnActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(activity1, "个性签名修改成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String s) {
                Toast.makeText(activity1, "个性签名修改失败" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeName() {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.p_change_name, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String changeName = ((EditText) view.findViewById(R.id.p_changeName)).getText().toString().trim();
                String changeInfo = ((EditText) view.findViewById(R.id.p_changeInfo)).getText().toString().trim();

                if ("".equals(changeName)) {
                    Toast.makeText(activity1, "昵称不能空", Toast.LENGTH_SHORT).show();
                } else {
                    tvName.setText(changeName);
                    tvInfo.setText(changeInfo);
                }
            }
        }).setNegativeButton("取消", null).create().show();
    }

    private void showDialog() {
        albumDialog = new AlertDialog.Builder(activity1)
                .create();
        albumDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_usericon, null);
        albumDialog.show();
        albumDialog.setContentView(view);

        ImageView albumImageView = (ImageView) view.findViewById(R.id.album_pic);
        ImageView cameraImageView = (ImageView) view.findViewById(R.id.camera_pic);

        albumImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoIntent = new Intent();
                photoIntent.setAction(Intent.ACTION_GET_CONTENT);
                photoIntent.setType("image/*");
                startActivityForResult(photoIntent, 2);
            }
        });
        cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumDialog.dismiss();
                Intent cameraIntent = new Intent();
                cameraIntent
                        .setAction(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // 保存照片到本地文件
                file = new File(path);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(file));
                startActivityForResult(cameraIntent,
                        1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (albumDialog.isShowing()) {
            albumDialog.dismiss();
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            myBitmap = ImageCompress.getimage(Uri.fromFile(file).getPath());
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
            ContentResolver resolver = getActivity().getContentResolver();
            Uri originalUri = data.getData(); // 获得图片的uri
//                Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            myBitmap = ImageCompress.getimage(getRealFilePath(getActivity(),originalUri));


            // 显得到bitmap图片
        }

        ivTouxiang.setImageBitmap(myBitmap);
        IMMyselfMainPhoto.upload(myBitmap, new IMSDK.OnActionProgressListener()
                {
                    @Override
                    public void onSuccess() {
                        p_tvProgress.setText("上传成功");
                        Toast.makeText(activity1, "上传成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(double v) {
                        p_tvProgress.setText("上传进度：" + (int) (v * 100) + "%");
                    }

                    @Override
                    public void onFailure(String s) {
                        p_tvProgress.setText("上传失败：" + s);
                        Toast.makeText(activity1, s, Toast.LENGTH_SHORT).show();
                    }
                }

        );


    }
    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
