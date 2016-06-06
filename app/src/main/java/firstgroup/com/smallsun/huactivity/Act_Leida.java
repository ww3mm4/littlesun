package firstgroup.com.smallsun.huactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import firstgroup.com.smallsun.R;

/**
 * Created by 胡加宏 on 2015/11/17.
 */
public class Act_Leida extends Activity implements View.OnClickListener {

    SearchDevicesView search_device_view;
    Button cancle;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_leida);
        search_device_view = (SearchDevicesView) findViewById(R.id.search_device_view);
        search_device_view.setWillNotDraw(false);
        cancle = (Button) findViewById(R.id.cancle);
        list = (ListView) findViewById(R.id.device_list_view);

        cancle.setOnClickListener(Act_Leida.this);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(4000);

                            Intent intent = new Intent(Act_Leida.this,Act_zhoubian.class);
                            startActivity(intent);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    public void onClick(View v) {
        switch(v.getId()){
            case R.id.cancle:
                search_device_view.setSearching(false);
                break;
        }
    }
}
