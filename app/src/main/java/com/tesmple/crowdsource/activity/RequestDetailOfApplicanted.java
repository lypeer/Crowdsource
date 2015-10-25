package com.tesmple.crowdsource.activity;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.AcceptableBillFragment;
import com.tesmple.crowdsource.fragment.AcceptedBillFragment;
import com.tesmple.crowdsource.fragment.BillCommentFragment;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.BillCommentUtils;
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
 * Created by ESIR on 2015/10/24.
 */
public class RequestDetailOfApplicanted extends AppCompatActivity {
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
     * bill的状态
     */
    private TextView tvStatus;

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

    /**
     * bill的取消报名按钮
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
                case StringUtils.CHANGE_APPLICANT_SUCCESSFULLY:
                    MainActivity.changeViewpagerItem(3);
                    finish();
                    break;
                case StringUtils.CHANGE_APPLICANT_FAILED:
                    Snackbar.make(btrCancelBill, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
                case StringUtils.CHANGE_BILL_STATUS_SUCCESSFULLY:
                    if(bill.getStatus().equals(StringUtils.BILL_STATUS_FOUR)){
                        PushUtils.startPushTransaction(handler,StringUtils.PUSH_CONFIRMER_REMOVE_BILL,bill);
                    }
                    finish();
                    break;
                case StringUtils.CHANGE_BILL_STATUS_FAILED:
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
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdetailofapplicanted);
        initView();
        initToolbar();
        getBundle();
        initTabAndViewPager();
        setView();
        startUpdateTime();
        setButtons();
    }

    /**
     * 绑定试图
     */
    private void initView(){
        sdvHeadPortrait = (SimpleDraweeView)findViewById(R.id.requestdetailofapplicanted_sdv_head_portrait);
        tvName = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_name);
        tvSchool = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_school);
        tvDetail = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_detail);
        tvAward = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_award);
        tvHour = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_left_time_hour);
        tvMinute = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_left_time_minutes);
        tvSecond = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_left_time_second);
        //btrPostApplication = (ButtonRectangle)findViewById(R.id.requestdetailofapplicanted_btr_postapplication);
        tvStatus = (TextView)findViewById(R.id.requestdetailofapplicanted_tv_status);
        btrCancelBill = (ButtonRectangle)findViewById(R.id.requestdetailofapplicanted_btr_cancelapplicant);
        liNeedgone = (LinearLayout)findViewById(R.id.li_needgone);
        tvInstead = (TextView)findViewById(R.id.tv_instead);
        liUpInfo = (LinearLayout)findViewById(R.id.li_upinfo);
        liDownInfo = (LinearLayout)findViewById(R.id.li_downinfo);
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
                        if(bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
                            new AlertDialog.Builder(RequestDetailOfApplicanted.this).setTitle(R.string.prompt_remind)
                                    .setMessage("确定取消报名")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            attemptCancelApplicant();
                                        }})
                                    .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();
                        }else if(bill.getStatus().equals(StringUtils.BILL_STATUS_TWO)){
                            new AlertDialog.Builder(RequestDetailOfApplicanted.this).setTitle(R.string.prompt_remind)
                                    .setMessage("不能完成了吗？记得联系分派任务者哦~")
                                    .setPositiveButton("无法完成", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            attempCancelBill();
                                        }})
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .show();
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    /**
     * 获取bundle值
     */
    private void getBundle(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bill = (Bill)bundle.getSerializable("bill");
    }

    /**
     * 初始化tablayout和viewpager
     */
    private void initTabAndViewPager(){
        tlComment = (TabLayout)findViewById(R.id.requestdetailofapplicanted_tl_compliantandcomment);
        vpComment = (ViewPager)findViewById(R.id.requestdetailofapplicanted_vp_compliantandcomment);
        initList();

        vpComment.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
        tlComment.setupWithViewPager(vpComment);
        tlComment.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化tablayout和fragment
     */
    private void initList(){
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        titleList.add("评论");
        fragmentList.add(new BillCommentFragment());
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

        if(bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
            btrCancelBill.setVisibility(View.GONE);
        }else if(bill.getStatus().equals(StringUtils.BILL_STATUS_TWO)){
            btrCancelBill.setText("联系TA");
        }else {
            btrCancelBill.setVisibility(View.GONE);
        }
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

    /**
     * 按钮绑定和设置
     */
    private void setButtons(){
        btrCancelBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill.getContactWay();
                new AlertDialog.Builder(RequestDetailOfApplicanted.this).setTitle(R.string.prompt_remind)
                        .setMessage("请选择联系方式")
                        .setPositiveButton("短信", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri smsToUri = Uri.parse("smsto:"+bill.getPublisherPhone());
                                Intent intent = new Intent( android.content.Intent.ACTION_SENDTO, smsToUri );
                                intent.putExtra("sms_body", "Hello~很高兴通过校园众包app认识你，有些事要跟你说哦。。。");
                                startActivity( intent );
                            }})
                        .setNegativeButton("电话", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*Intent intent=new Intent("android.intent.action.CALL",Uri.parse("tel:"+bill.getPublisherPhone()));
                                startActivity(intent);*/
                                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + bill.getPublisherPhone()));
                                startActivity(intent);
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                /*if(bill.getStatus().equals(StringUtils.BILL_STATUS_ONE)){
                    new AlertDialog.Builder(RequestDetailOfApplicanted.this).setTitle(R.string.prompt_remind)
                            .setMessage("确定取消报名")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attemptCancelApplicant();
                                }})
                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }else if(bill.getStatus().equals(StringUtils.BILL_STATUS_TWO)){
                    new AlertDialog.Builder(RequestDetailOfApplicanted.this).setTitle(R.string.prompt_remind)
                            .setMessage("完成当前到期任务吗？如果选择未完成请及时与发单者联系")
                            .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attempCompliteBill();
                                }})
                            .setNegativeButton("未完成", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    attemptCancelApplicant();
                                }
                            }).
                            setNeutralButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }*/
            }
        });

        liNeedgone = (LinearLayout)findViewById(R.id.li_needgone);
        tvInstead = (TextView)findViewById(R.id.tv_instead);
        tvInstead.setVisibility(View.GONE);
        tvInstead.setHeight(444);
        ButtonFlat btflatUpOrDown = (ButtonFlat)findViewById(R.id.btflat_up_or_down);
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
     * 尝试取消报名
     */
    private void attemptCancelApplicant(){
        BillUtils.changeApplicantor(handler,bill,false);
    }

    /**
     * 尝试完成订单
     */
    private void attempCompliteBill(){
        bill.setStatus(StringUtils.BILL_STATUS_THREE);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_THREE);
    }

    /**
     * 尝试未完成订单
     */
    private void attempCancelBill(){
        bill.setStatus(StringUtils.BILL_STATUS_FOUR);
        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_FOUR);
    }

}
