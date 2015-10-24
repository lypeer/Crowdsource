package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.BillCommentFragment;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.PushUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.validation.TypeInfoProvider;

/**
 * Created by ESIR on 2015/10/19.
 */
public class RequestDetailOfApplicant extends AppCompatActivity {
    /**
     * 详情界面的tablayout
     */
    private TabLayout tlComment;

    /**
     * 详情界面的viwepager
     */
    private ViewPager vpComment;

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
     * bill详情
     */
    private TextView tvDetail;

    /**
     * 可接订单进入到订单详情后的报名按钮
     */
    private ButtonRectangle btrPostApplication;

    /**
     * publisher看到的bill详情的目标bill
     */
    private Bill bill;

    /**
     * bill的奖励textview
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

    public final Handler handler = new android.os.Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
                    tvHour.setText(timeList.get(0));
                    tvMinute.setText(timeList.get(1));
                    tvSecond.setText(timeList.get(2));
                    break;
                case StringUtils.CHANGE_APPLICANT_SUCCESSFULLY:
                    if (bill.getRobType().equals(getString(R.string.bill_robtype_grabbillmode))){
                        bill.setConfirmer(User.getInstance().getUserName());
                        BillUtils.changeBillStatus(handler,bill,StringUtils.BILL_STATUS_TWO);
                    }//抢单模式
                    else if (bill.getRobType().equals(getString(R.string.bill_robtype_receivebillmode))){
                        Toast.makeText(App.getContext(),"报名成功！",Toast.LENGTH_LONG).show();
                        PushUtils.startPushTransaction(handler , StringUtils.PUSH_BECOME_APPLICANT , bill);
                        finish();
                    }//接单模式
                    break;
                case StringUtils.CHANGE_BILL_STATUS_SUCCESSFULLY:
                    finish();
                    break;
                case StringUtils.CHANGE_APPLICANT_FAILED:
                    Snackbar.make(btrPostApplication,getString(R.string.please_check_your_network),Snackbar.LENGTH_LONG).show();
                    break;
                case StringUtils.CHANGE_BILL_STATUS_FAILED:
                    Snackbar.make(btrPostApplication,getString(R.string.please_check_your_network),Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdetailofapplicant);
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
        sdvHeadPortrait = (SimpleDraweeView)findViewById(R.id.requestdetailofapplicant_sdv_head_portrait);
        tvName = (TextView)findViewById(R.id.requestdetailofapplicant_tv_name);
        tvSchool = (TextView)findViewById(R.id.requestdetailofapplicant_tv_school);
        tvDetail = (TextView)findViewById(R.id.requestdetailofapplicant_tv_detail);
        tvAward = (TextView)findViewById(R.id.requestdetailofapplicant_tv_award);
        tvHour = (TextView)findViewById(R.id.requestdetailofapplicant_tv_left_time_hour);
        tvMinute = (TextView)findViewById(R.id.requestdetailofapplicant_tv_left_time_minutes);
        tvSecond = (TextView)findViewById(R.id.requestdetailofapplicant_tv_left_time_second);
        btrPostApplication = (ButtonRectangle)findViewById(R.id.requestdetailofapplicant_btr_postapplication);
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
     * 初始化Tab和ViewPager
     */
    private void initTabAndViewPager(){
        tlComment = (TabLayout)findViewById(R.id.requestdetailofapplicant_tl_compliantandcomment);
        vpComment = (ViewPager)findViewById(R.id.requestdetailofapplicant_vp_compliantandcomment);
        initList();

        vpComment.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
        tlComment.setupWithViewPager(vpComment);
        tlComment.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化TabLayout和ViewPager
     */
    private void initList(){
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        titleList.add("评论");

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
        tvDetail.setText(bill.getDetail());
        tvAward.setText(bill.getAward());
        ArrayList<String> timeList = new ArrayList<String>();
        timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
        tvHour.setText(timeList.get(0));
        tvMinute.setText(timeList.get(1));
        tvSecond.setText(timeList.get(2));
    }

    private void setButtons(){
        btrPostApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempPostApplication();
            }
        });
    }

    /**
     * 尝试报名
     */
    private void attempPostApplication(){
        BillUtils.changeApplicantor(handler, bill, true);
    }
}
