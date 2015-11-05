package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lypeer on 11/5/2015.
 */
public class ApplicantSuccessfullyActivity extends AppCompatActivity {
    /**
     * billdeadline的string值
     */
    private String stBillDeadLine;

    /**
     * 剩余时间
     */
    private String timeLeft;

    /**
     * 倒计时中的小时
     */
    private TextView tvHour;

    /**
     * 倒计时中的分钟
     */
    private TextView tvMinute;

    /**
     * 倒计时中的秒
     */
    private TextView tvSecond;

    /**
     * 剩余时间list，0为小时数，1为分钟数，2为秒数
     */
    private ArrayList<String> timeList = new ArrayList<String>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case StringUtils.postSuccessfully_needchangetime:
                    setDeadline();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_successfully);
        ActivityCollector.addActivity(ApplicantSuccessfullyActivity.this);
        getBundleInfo();
        initView();
        initToolbar();
        //setDeadline();
        starDeadline();
//        goBack();
    }

    /**
     * 获取到bundle信息
     */
    private void getBundleInfo() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        stBillDeadLine = bundle.getString(getResources().getString(R.string.billDeadLine));
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.prompt_applicant_successflly);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 视图绑定
     */
    private void initView() {
        tvHour = (TextView) findViewById(R.id.applicant_successfully_tv_hour);
        tvMinute = (TextView) findViewById(R.id.applicant_successfully_tv_minute);
        tvSecond = (TextView) findViewById(R.id.applicant_successfully_tv_second);

        findViewById(R.id.applicant_successfully_btn_to_by_bill).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ApplicantSuccessfullyActivity.this  , MainActivity.class);
//                startActivity(intent);
                finish();
                MainActivity.changeViewpagerItem(2);
            }
        });
    }

    /**
     * 设置倒计时信息
     */
    private void setDeadline() {
        tvHour.setText(timeList.get(0));
        tvMinute.setText(timeList.get(1));
        tvSecond.setText(timeList.get(2));
    }

    private void starDeadline() {
        Timer time = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                timeList = TimeUtils.long2hourminutesecond(Long.valueOf(stBillDeadLine) - System.currentTimeMillis());
                Message message = new Message();
                message.what = StringUtils.postSuccessfully_needchangetime;
                handler.sendMessage(message);
            }
        };
        time.schedule(timerTask, 0, 1000);
    }

    private void goBack() {
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ApplicantSuccessfullyActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        //timer.schedule(timerTask, 1500);
    }

    @Override
    public void finish() {
        MainActivity.changeViewpagerItem(1);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
