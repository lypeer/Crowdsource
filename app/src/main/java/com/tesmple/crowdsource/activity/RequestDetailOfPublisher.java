package com.tesmple.crowdsource.activity;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.ButtonFlat;
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
import com.tesmple.crowdsource.utils.PushUtils;
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

    /**
     * 需要操纵的linearlayout
     */
    private LinearLayout liNeedgone;

    /**
     * 用来代替的textview
     */
    private TextView tvInstead;

    /**
     * 订单详情是否展开
     */
    boolean isRequestShow = true;

    /**
     * 收起按钮显示内容
     */
    private LinearLayout liUpInfo;

    /**
     * 展开按钮显示内容
     */
    private LinearLayout liDownInfo;

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
                    if(bill.getStatus().equals(StringUtils.BILL_STATUS_THREE)){
                        PushUtils.startPushTransaction(handler , StringUtils.PUSH_FINISH_BILL , bill);
                    }
                    if(bill.getStatus().equals(StringUtils.BILL_STATUS_FOUR)){
                        PushUtils.startPushTransaction(handler , StringUtils.PUSH_PUBLISHER_REMOVE_BILL , bill);
                    }
                    finish();
                    break;
                case StringUtils.CHANGE_APPLICANT_FAILED:
                    Snackbar.make(btrCancelBill, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
                case StringUtils.POST_REQUEST_FAILED:
                    Snackbar.make(btrCancelBill, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
                case -1:
                    liNeedgone.setVisibility(View.GONE);
                    tvInstead.setVisibility(View.VISIBLE);
                    tvInstead.setHeight(444);
                    Log.i("height2", String.valueOf(444));
                    Log.i("tvinstead", String.valueOf(tvInstead.getHeight()));
                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(tvInstead, "Height", 444 , 0);
                    objectAnimator.setDuration(300);
                    objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                    objectAnimator.start();
                    liUpInfo.setVisibility(View.GONE);
                    liDownInfo.setVisibility(View.VISIBLE);
                    break;
                case -2:
                    tvInstead.setVisibility(View.GONE);
                    liNeedgone.setAlpha(0f);
                    liNeedgone.setVisibility(View.VISIBLE);
                    ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(liNeedgone, "Alpha", 0.0f, 1.0f);
                    objectAnimator1.setDuration(300);
                    objectAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
                    objectAnimator1.start();
                    liUpInfo.setVisibility(View.VISIBLE);
                    liDownInfo.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdetailofpublisher);
        ActivityCollector.addActivity(RequestDetailOfPublisher.this);
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
        liNeedgone = (LinearLayout)findViewById(R.id.li_needgone);
        tvInstead = (TextView)findViewById(R.id.tv_instead);
        liUpInfo = (LinearLayout)findViewById(R.id.li_upinfo);
        liDownInfo = (LinearLayout)findViewById(R.id.li_downinfo);
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

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_delete:
                        deleteBill();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 撤销订单的方法
     */
    private void deleteBill(){
        if (bill.getStatus().equals(StringUtils.BILL_STATUS_TWO)) {
            new AlertDialog.Builder(this).setTitle(R.string.prompt_remind)
                    .setMessage("取消当前进行中任务？如果取消了任务请及时联系接单者").setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    attempCancelBill();
                }
            }).setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }else if(bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
            new AlertDialog.Builder(this).setTitle(R.string.prompt_remind)
                    .setMessage("取消当前待报名任务？").setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    attempCancelBill();
                }
            }).setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        } else if (bill.getStatus().equals(StringUtils.BILL_STATUS_FOUR)||bill.getStatus().equals(StringUtils.BILL_STATUS_THREE)) {
            new AlertDialog.Builder(this).setTitle(R.string.prompt_remind)
                    .setMessage("删除当前任务？").setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    attempDeleteBill();
                }
            }).setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
        }
    }

    /**
     * 设置bill详情
     */
    private void setView(){
        sdvHeadPortrait.setImageURI(Uri.parse(AVUser.getCurrentUser().getAVFile("head_portrait").getUrl()));
        tvName.setText(AVUser.getCurrentUser().get("nickname").toString());
        tvSchool.setText(bill.getPublisherSchool());
        tvStatus.setText(bill.getStatus());
        tvDetail.setText(bill.getDetail());
        tvAward.setText(bill.getAward());
        ArrayList<String> timeList = new ArrayList<String>();
        timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
        tvHour.setText(timeList.get(0));
        tvMinute.setText(timeList.get(1));
        tvSecond.setText(timeList.get(2));

        if(bill.getStatus().equals(StringUtils.BILL_STATUS_TWO)){
            btrCancelBill.setText("完成任务？");
        }else {
            btrCancelBill.setVisibility(View.GONE);
        }
        /*if (bill.getStatus().equals(StringUtils.BILL_STATUS_TWO) || bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
            btrCancelBill.setText("取消任务");
        }
        if (bill.getStatus().equals(StringUtils.BILL_STATUS_FOUR)){
            btrCancelBill.setText("删除任务");
        }*/
    }

    /**
     * 设置button按钮监听及相关
     */
    private void setButtons(){
        btrCancelBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvHour.getText().equals("0") && tvMinute.getText().equals("0") && tvMinute.getText().equals("0")) {
                    new AlertDialog.Builder(RequestDetailOfPublisher.this).setTitle(R.string.prompt_remind)
                            .setMessage("完成当前到期任务吗？如果选择未完成请及时与接单者联系")
                            .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attempCompliteBill();
                                }
                            })
                            .setNegativeButton("未完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attempCancelBill();
                                }
                            }).
                            setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(RequestDetailOfPublisher.this).setTitle(R.string.prompt_remind)
                            .setMessage("完成当前任务吗？")
                            .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attempCompliteBill();
                                }
                            })
                            .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });
        ButtonFlat btflatUpOrDown = (ButtonFlat)findViewById(R.id.btflat_up_or_down);
        liNeedgone = (LinearLayout)findViewById(R.id.li_needgone);
        tvInstead = (TextView)findViewById(R.id.tv_instead);
        tvInstead.setVisibility(View.GONE);
        tvInstead.setHeight(444);
        btflatUpOrDown.setRippleSpeed(60.0f);
        btflatUpOrDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starAnimation();
            }
        });
    }

    /**
     * 开始收起展开动画
     */
    private void starAnimation(){
        if(isRequestShow){
            btrCancelBill.setClickable(false);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(liNeedgone, "Alpha", 1.0f, 0.0f);
            objectAnimator.setDuration(300);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();
            isRequestShow = false;
            Timer timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = -1;
                    handler.sendMessage(message);
                }
            };
            timer.schedule(timerTask,300);
        }
        else {
            btrCancelBill.setClickable(true);
            tvInstead.setVisibility(View.VISIBLE);
            tvInstead.setHeight(0);
            liNeedgone.setAlpha(0f);
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(liNeedgone, "Alpha", 0.0f, 1.0f);
            objectAnimator.setDuration(300);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.start();
            isRequestShow = true;
            Timer timer = new Timer(true);
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = -2;
                    handler.sendMessage(message);
                }
            };
            timer.schedule(timerTask,300);
        }
    }

    /**
     * 尝试取消bill
     */
    private void attempCancelBill(){
        bill.setStatus(StringUtils.BILL_STATUS_FOUR);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_FOUR);
    }

    /**
     * 尝试删除bill
     */
    private void attempDeleteBill(){
        bill.setStatus(StringUtils.BILL_STATUS_FIVE);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_FIVE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    /**
     * 尝试完成订单
     */
    private void attempCompliteBill(){
        bill.setStatus(StringUtils.BILL_STATUS_THREE);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_THREE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
