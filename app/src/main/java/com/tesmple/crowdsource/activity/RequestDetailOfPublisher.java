package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.AcceptableBillFragment;
import com.tesmple.crowdsource.fragment.AcceptedBillFragment;
import com.tesmple.crowdsource.fragment.ApplicantFragment;
import com.tesmple.crowdsource.fragment.BillCommentFragment;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ESIR on 2015/10/17.
 */
public class RequestDetailOfPublisher extends AppCompatActivity {

    /**
     * 评论区的tablayout
     */
    private TabLayout tlCompliantAndComment;

    /**
     * 评论区的viwepager
     */
    private ViewPager vpCompliantAndComment;

    /**
     * 装viewpager的每一个界面的list
     */
    private List<String> titleList;

    /**
     * 装fragment的每一个对象的list
     */
    private List<Fragment> fragmentList;

    /**
     * bill详情里的publisher头像
     */
    private SimpleDraweeView sdvHeadPortrait;

    /**
     * bill详情里的publisher名字
     */
    private TextView tvName;

    /**
     * bill详情里的publisher学校名字
     */
    private TextView tvSchool;

    /**
     * bill状态
     */
    private TextView tvStatus;

    /**
     * bill详情
     */
    private TextView tvDetail;

    /**
     * publisher看到的bill详情的目标bill
     */
    private Bill bill;

    /**
     * bill的奖励TextView
     */
    private TextView tvAward;

    /**
     * 小时倒计时
     */
    private TextView tvHour;

    /**
     * 分钟倒计时
     */
    private TextView tvMinute;

    /**
     * 秒钟倒计时
     */
    private TextView tvSecond;

    /**
     * 剩余时间list，0为小时数，1为分钟数，2为秒数
     */
    private ArrayList<String> timeList = new ArrayList<String>();

    /**
     * 取消任务的buttonrectangle
     */
    private ButtonRectangle btrCancelBill;


    public final Handler handler = new android.os.Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
                    tvHour.setText(timeList.get(0));
                    tvMinute.setText(timeList.get(1));
                    tvSecond.setText(timeList.get(2));
                    break;
                case StringUtils.CHANGE_BILL_STATUS_SUCCESSFULLY:
                    AcceptableBillFragment.notifyDateChanged();
                    MyPublishFragment.notifyDateChanged();
                    AcceptedBillFragment.notifyDateChanged();
                    finish();
                    break;
                case StringUtils.CHANGE_APPLICANT_FAILED:
                    Snackbar.make(btrCancelBill, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
                case StringUtils.POST_REQUEST_FAILED:
                    Snackbar.make(btrCancelBill, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdetailofpublisher);
        initView();
        getBundle();
        initToolbar();
        initTabAndViewPager();
        setView();
        startUpdateTime();
        setButtons();
    }

    /**
     * 开始倒计时
     */
    private void startUpdateTime() {
        Timer timer = new Timer(true);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    private void initView(){
        sdvHeadPortrait = (SimpleDraweeView)findViewById(R.id.requestdetailofpublisher_sdv_head_portrait);
        tvName = (TextView)findViewById(R.id.requestdetailofpublisher_tv_name);
        tvSchool = (TextView)findViewById(R.id.requestdetailofpublisher_tv_school);
        tvDetail = (TextView)findViewById(R.id.requestdetailofpublisher_tv_detail);
        tvAward = (TextView)findViewById(R.id.requestdetailofpublisher_tv_award);
        tvHour = (TextView)findViewById(R.id.requestdetailofpublisher_tv_left_time_hour);
        tvMinute = (TextView)findViewById(R.id.requestdetailofpublisher_tv_left_time_minutes);
        tvSecond = (TextView)findViewById(R.id.requestdetailofpublisher_tv_left_time_second);
        btrCancelBill = (ButtonRectangle)findViewById(R.id.requestdetailofpublisher_btr_cancelbill);
        tvStatus = (TextView)findViewById(R.id.requestdetailofpublisher_tv_status);
    }

    /**
     * 获取intent里面的bundle
     */
    private void getBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bill = (Bill)bundle.getSerializable("bill");
    }

    /**
     * 初始化tab和viewpager
     */
    private void initTabAndViewPager(){
        tlCompliantAndComment = (TabLayout)findViewById(R.id.requestdetailofpublisher_tl_compliantandcomment);
        vpCompliantAndComment = (ViewPager)findViewById(R.id.requestdetailofpublisher_vp_compliantandcomment);
        initList();

        vpCompliantAndComment.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
        tlCompliantAndComment.setupWithViewPager(vpCompliantAndComment);
        tlCompliantAndComment.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化tablayout列表和fragment列表
     */
    private void initList(){
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        titleList.add("报名者");
        titleList.add("评论");

        fragmentList.add(new ApplicantFragment());
        fragmentList.add(new BillCommentFragment());
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("任务详情");
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
     * 设置bill详情
     */
    private void setView(){
        sdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
        tvName.setText(bill.getPublisherName());
        tvSchool.setText(bill.getPublisherSchool());
        tvStatus.setText(bill.getStatus());
        tvDetail.setText(bill.getDetail());
        tvAward.setText(bill.getAward());
        ArrayList<String> timeList = new ArrayList<String>();
        timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
        tvHour.setText(timeList.get(0));
        tvMinute.setText(timeList.get(1));
        tvSecond.setText(timeList.get(2));

        if (bill.getStatus().equals(StringUtils.BILL_STATUS_TWO) || bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
            btrCancelBill.setText("取消任务");
        }
        if (bill.getStatus().equals(StringUtils.BILL_STATUS_FOUR)){
            btrCancelBill.setText("删除任务");
        }
    }

    /**
     * 设置button按钮监听及相关
     */
    private void setButtons(){
        btrCancelBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bill.getStatus().equals(StringUtils.BILL_STATUS_TWO) || bill.getStatus().equals(StringUtils.BILL_STATUS_ONE) ){
                    attempCancelBill();
                }else if (bill.getStatus().equals(StringUtils.BILL_STATUS_FOUR)){
                    attempDeleteBill();
                }
            }
        });
    }

    /**
     * 尝试取消bill
     */
    private void attempCancelBill(){
        bill.setStatus(StringUtils.BILL_STATUS_FOUR);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_FOUR);
        MyPublishFragment.notifyDateChanged();
    }

    /**
     * 尝试删除bill
     */
    private void attempDeleteBill(){
        bill.setStatus(StringUtils.BILL_STATUS_FIVE);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_FIVE);
    }
}
