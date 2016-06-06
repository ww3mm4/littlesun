package firstgroup.com.smallsun.shequ;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import firstgroup.com.smallsun.R;
import firstgroup.com.smallsun.adapter.AllPinglun_Adapter;
import imsdk.data.community.CMCommInfo;
import imsdk.data.community.CMUserInfo;
import imsdk.data.community.IMCommunity;

/**
 * Created by Cherrys on 2015/11/18.
 */
public class AllPinglunByIdActivity extends AppCompatActivity{
    private  List<CMCommInfo> data = new ArrayList<CMCommInfo>();
    private ListView all_list;
    private AllPinglun_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_pinglun_activity);
        setTitle("评论列表");
        all_list = (ListView) findViewById(R.id.all_pinglunLv);

        adapter = new AllPinglun_Adapter(data,this);
        all_list.setAdapter(adapter);

        long topicId = getIntent().getLongExtra("topicId",0);

        getAllPinglun(topicId);


    }

    private void getAllPinglun(long topicId) {
        //拉取类型，默认向前
        IMCommunity.getNextCommentList(20, topicId, new IMCommunity.OnGetCommInfoListListener() {
            @Override
            public void onSuccess(List list, boolean b) {
                List<CMCommInfo> info = list;
                data.addAll(info);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AllPinglunByIdActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
