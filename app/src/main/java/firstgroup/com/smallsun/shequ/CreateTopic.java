package firstgroup.com.smallsun.shequ;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.utils.ImageCompress;
import firstgroup.com.view.CustomDialog;
import imsdk.data.IMMyself;
import imsdk.data.community.IMCommunity;
import imsdk.data.group.IMGroupInfo;

/**
 * Created by Cherrys on 2015/11/18.
 */
public class CreateTopic extends AppCompatActivity implements View.OnClickListener {
    private GridView gv;
    private EditText etTitle,etContent;
    private String pathImage;
    private Bitmap bmp;
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;
    private final int IMAGE_OPEN = 1;
    private  ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_create_top);

        setActionBar();  //设置自定义actionBar

        initView();
        initgrid();
    }
    private void setActionBar() {
        View view = getLayoutInflater().inflate(R.layout.qun_title,null);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(view);

        ImageView iv_back = (ImageView) view.findViewById(R.id.c_q_titleBack);
        TextView tvTitle = (TextView) view.findViewById(R.id.c_q_tvTitle);

        tvTitle.setText("创建话题");
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initView() {
        ((Button) findViewById(R.id.s_createTopic)).setOnClickListener(this);
        etTitle = (EditText) findViewById(R.id.s_createTopicTitle);
        etContent = (EditText) findViewById(R.id.s_createTopicContent);
        gv = (GridView) findViewById(R.id.s_creategridview);
    }
    //与GridView有
    private void initgrid(){
        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic); //�Ӻ�
        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageView1});
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    Bitmap bm = ThumbnailUtils.extractThumbnail((Bitmap)data, 80, 80);
                    i.setImageBitmap( bm);
                    return true;
                }
                return false;
            }
        });
        gv.setAdapter(simpleAdapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 10) {
                    Toast.makeText(CreateTopic.this, "最多只能有9张图", Toast.LENGTH_SHORT).show();
                } else if (position == 0) {
                    Toast.makeText(CreateTopic.this, "请选择图片", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, IMAGE_OPEN);
                    overridePendingTransition(
                            R.anim.block_move_right,R.anim.small_2_big);
                } else {
                    dialog(position);
                    //Toast.makeText(MainActivity.this, "�����" + (position + 1) + " ��ͼƬ",
                    //		Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode==IMAGE_OPEN) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getAuthority())) {

                Cursor cursor = getContentResolver().query(
                        uri,
                        new String[] { MediaStore.Images.Media.DATA },
                        null,
                        null,
                        null);
                if (null == cursor) {
                    return;
                }
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
    }
    private  void getBitmaps(){

        for (int i=0;i<imageItem.size();i++){
            HashMap<String, Object> map = imageItem.get(i);
            if (i!=0) {
                for (String key : map.keySet()) {
                    if (map.get(key) instanceof Bitmap) {
                        Bitmap bm = ThumbnailUtils.extractThumbnail((Bitmap) map.get(key), 80, 80);
                        bitmaps.add(bm);
                    }
                }
            }
        }

    }

    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(pathImage)){
            Bitmap bitmap = ImageCompress.getimage(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", bitmap);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gv.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            pathImage = null;
        }
    }
    protected void dialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否删除此图片");
        builder.setTitle("是否删除");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.s_createTopic:
                String title = etTitle.getText().toString().trim();
                String content = etContent.getText().toString().trim();
                if ("".equals(title)|"".equals(content)){
                    Toast.makeText(this,"标题或内容不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    createTopic(title,content);
                }
                break;
        }
    }

    private void createTopic(String title,String content) {
        final Dialog dialog = loadingDialog(this);
        getBitmaps();
        IMCommunity.createTopic(title, content, bitmaps,
                new IMMyself.OnActionResultListener() {
                    @Override
                    public void onSuccess(Object result) {
                        if (dialog.isShowing()){
                            dialog.dismiss();
                        }
                        Toast.makeText(CreateTopic.this, "发表成功了", Toast.LENGTH_SHORT).show();
                        //创建成功
                        finish();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(CreateTopic.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private Dialog loadingDialog(Context context) {
        CustomDialog customDialog = new CustomDialog(context, R.layout.dialog_layout,
                R.style.DialogTheme);
        customDialog.show();
        customDialog.setCancelable(false);
        customDialog.setCanceledOnTouchOutside(false);
        return customDialog;
    }

}
