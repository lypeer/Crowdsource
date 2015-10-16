package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.ActivityCollector;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ESIR on 2015/10/12.
 */
public class PostBillSuccessful extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postbillsuccessful);
        ActivityCollector.addActivity(this);
        initToolbar();
        goBack();
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("发布成功");
        toolbar.setLogo(R.drawable.ic_back);
        setSupportActionBar(toolbar);
    }

    private void goBack(){
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(PostBillSuccessful.this,MyPublishFragment.class);
                startActivity(intent);
                finish();
            }
        };
        timer.schedule(timerTask, 1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
