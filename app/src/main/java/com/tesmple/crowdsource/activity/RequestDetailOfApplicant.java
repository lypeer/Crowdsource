package com.tesmple.crowdsource.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
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
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.AcceptableAdapter;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.AcceptableBillFragment;
import com.tesmple.crowdsource.fragment.AcceptedBillFragment;
import com.tesmple.crowdsource.fragment.BillCommentFragment;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.PushUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.TimeUtils;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.umeng.analytics.MobclickAgent;

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
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";

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
     * bill状态
     */
    private TextView tvStatus;

    /**
     * 剩余时间list，0为小时数，1为分钟数，2为秒数
     */
    private ArrayList<String> timeList = new ArrayList<String>();

    /**
     * 订单详情是否展开
     */
    public boolean isRequestShow = true;

    /**
     * 需要操纵的linearlayout
     */
    private LinearLayout liNeedgone;

    /**
     * 用来代替的textview
     */
    private TextView tvInstead;

    /**
     * 收起按钮显示内容
     */
    private LinearLayout liUpInfo;

    /**
     * 展开按钮显示内容
     */
    private LinearLayout liDownInfo;

    private int drawingStartLocation;

    /**
     * 背景的布局
     */
    private LinearLayout llyBackground;

    /**
     * 主界面的布局
     */
    private LinearLayout llyMain;

    public final Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
                    tvHour.setText(timeList.get(0));
                    tvMinute.setText(timeList.get(1));
                    tvSecond.setText(timeList.get(2));
                    break;
                case StringUtils.CHANGE_APPLICANT_SUCCESSFULLY:
                    if (bill.getRobType().equals(getString(R.string.bill_robtype_grabbillmode))) {
                        bill.setConfirmer(User.getInstance().getUserName());
                        BillUtils.changeBillStatus(handler, bill, StringUtils.BILL_STATUS_TWO);
                        PushUtils.startPushTransaction(handler, StringUtils.PUSH_HAVE_ROBBED, bill);
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.billDeadLine), bill.getDeadline().toString());
                        Intent intent = new Intent(RequestDetailOfApplicant.this, RobBillSuccessfullyActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }//抢单模式
                    else if (bill.getRobType().equals(getString(R.string.bill_robtype_receivebillmode))) {
                        Toast.makeText(App.getContext(), "报名成功！", Toast.LENGTH_LONG).show();
                        PushUtils.startPushTransaction(handler, StringUtils.PUSH_BECOME_APPLICANT, bill);
                        finish();
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.billDeadLine), bill.getDeadline().toString());
                        Intent intent = new Intent(RequestDetailOfApplicant.this, ApplicantSuccessfullyActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }//接单模式
                    break;
                case StringUtils.CHANGE_BILL_STATUS_SUCCESSFULLY:
                    finish();
                    break;
                case StringUtils.CHANGE_APPLICANT_FAILED:
                    Snackbar.make(btrPostApplication, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
                case StringUtils.CHANGE_BILL_STATUS_FAILED:
                    Snackbar.make(btrPostApplication, getString(R.string.please_check_your_network), Snackbar.LENGTH_LONG).show();
                    break;
                case -1:
                    liNeedgone.setVisibility(View.GONE);
                    tvInstead.setVisibility(View.VISIBLE);
                    tvInstead.setHeight(444);
                    Log.i("height2", String.valueOf(444));
                    Log.i("tvinstead", String.valueOf(tvInstead.getHeight()));
                    ObjectAnimator objectAnimator = ObjectAnimator.ofInt(tvInstead, "Height", 444, 0);
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
        setContentView(R.layout.activity_requestdetailofapplicant);

        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        llyBackground = (LinearLayout) findViewById(R.id.request_detail_of_applicant_lly_background);
        llyMain = (LinearLayout) findViewById(R.id.request_detail_of_applicant_lly_main);
        //为了显示动画先将主界面干掉
        llyBackground.setVisibility(View.VISIBLE);
        llyMain.setVisibility(View.GONE);
        if (savedInstanceState == null) {
            llyBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    llyBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }

        ActivityCollector.addActivity(RequestDetailOfApplicant.this);
        initView();
        getBundle();
        initToolbar();
        initTabAndViewPager();
        setView();
        startUpdateTime();
        setButtons();
    }

    private void startIntroAnimation() {
        llyBackground.setScaleY(0.1f);
        llyBackground.setPivotY(drawingStartLocation);

        llyBackground.animate()
                .scaleY(1)
                .setDuration(300)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animateContent();
                    }
                })
                .start();
    }

    private void animateContent() {
        llyBackground.setVisibility(View.GONE);
        llyMain.setVisibility(View.VISIBLE);
        llyMain.setBackgroundColor(App.getContext().getResources().getColor(R.color.bg_wechat));

        animateMain();
    }

    /**
     * 主界面显示之后加载的动画
     */
    private void animateMain() {
        final View card = findViewById(R.id.request_detail_of_applicant_cv_bill);
        final View other = findViewById(R.id.request_detail_of_applicant_cv_other);
        card.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llyBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                AnimatorSet set = new AnimatorSet();
                ObjectAnimator transAnimBill = ObjectAnimator.ofFloat(card,
                        "Y", (4 * Resources.getSystem().getDisplayMetrics().density) - 300, (4 * Resources.getSystem().getDisplayMetrics().density));
                transAnimBill.setDuration(350);

                ObjectAnimator transAnimOther = ObjectAnimator.ofFloat(other,
                        "Y", card.getHeight() + findViewById(R.id.toolbar).getHeight() +
                                (12 * Resources.getSystem().getDisplayMetrics().density) + 300,
                        card.getHeight() + findViewById(R.id.toolbar).getHeight() +
                                (12 * Resources.getSystem().getDisplayMetrics().density));
                transAnimOther.setDuration(500);

                set.setInterpolator(new DecelerateInterpolator());
                set.play(transAnimBill).with(transAnimOther);
                set.start();
            }
        });
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
     * 绑定视图
     */
    private void initView() {
        sdvHeadPortrait = (SimpleDraweeView) findViewById(R.id.requestdetailofapplicant_sdv_head_portrait);
        tvName = (TextView) findViewById(R.id.requestdetailofapplicant_tv_name);
        tvSchool = (TextView) findViewById(R.id.requestdetailofapplicant_tv_school);
        tvDetail = (TextView) findViewById(R.id.requestdetailofapplicant_tv_detail);
        tvAward = (TextView) findViewById(R.id.requestdetailofapplicant_tv_award);
        tvHour = (TextView) findViewById(R.id.requestdetailofapplicant_tv_left_time_hour);
        tvMinute = (TextView) findViewById(R.id.requestdetailofapplicant_tv_left_time_minutes);
        tvSecond = (TextView) findViewById(R.id.requestdetailofapplicant_tv_left_time_second);
        btrPostApplication = (ButtonRectangle) findViewById(R.id.requestdetailofapplicant_btr_postapplication);
        tvStatus = (TextView) findViewById(R.id.requestdetailofapplicant_tv_status);
        liUpInfo = (LinearLayout) findViewById(R.id.li_upinfo);
        liDownInfo = (LinearLayout) findViewById(R.id.li_downinfo);
    }

    /**
     * 获取intent里面的bundle
     */
    private void getBundle() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bill = (Bill) bundle.getSerializable("bill");
    }

    /**
     * 初始化Tab和ViewPager
     */
    private void initTabAndViewPager() {
        tlComment = (TabLayout) findViewById(R.id.requestdetailofapplicant_tl_compliantandcomment);
        vpComment = (ViewPager) findViewById(R.id.requestdetailofapplicant_vp_compliantandcomment);
        initList();

        vpComment.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
        tlComment.setupWithViewPager(vpComment);
        tlComment.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * 初始化TabLayout和ViewPager
     */
    private void initList() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        titleList.add("评论");

        fragmentList.add(new BillCommentFragment());
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
    private void setView() {
        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.whereEqualTo("username", bill.getPublisherPhone());
        avQuery.setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    bill.setPublisherName((String) list.get(0).get("nickname"));
                    bill.setPublisherHeadPortrait(list.get(0).getAVFile("head_portrait").getThumbnailUrl(false, 96, 96));
                    sdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
                    tvName.setText(bill.getPublisherName());

                } else {
                    Log.e("RequestDetailAppliError", e.getMessage() + "===" + e.getCode());
                    //没有缓存数据
                    if (e.getCode() != 120) {
                        Snackbar.make(sdvHeadPortrait, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
        tvSchool.setText(bill.getPublisherSchool());
        tvDetail.setText(bill.getDetail());
        tvAward.setText(bill.getAward());
        tvStatus.setText(bill.getStatus());
        ArrayList<String> timeList = new ArrayList<String>();
        timeList = TimeUtils.long2hourminutesecond(bill.getDeadline() - System.currentTimeMillis());
        tvHour.setText(timeList.get(0));
        tvMinute.setText(timeList.get(1));
        tvSecond.setText(timeList.get(2));

        if(bill.getRobType().equals(App.getContext().getString(R.string.bill_robtype_receivebillmode))){
            findViewById(R.id.request_detail_of_applicant_iv_bill_type).
                    setBackground(App.getContext().getResources().getDrawable(R.drawable.prompt_accept));
        }else {
            findViewById(R.id.request_detail_of_applicant_iv_bill_type).
                    setBackground(App.getContext().getResources().getDrawable(R.drawable.prompt_rob));
        }
    }

    /**
     * 设置按钮监听事件及相关
     */
    private void setButtons() {
        btrPostApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempPostApplication();
            }
        });
        ButtonFlat btflatUpOrDown = (ButtonFlat) findViewById(R.id.btflat_up_or_down);
        liNeedgone = (LinearLayout) findViewById(R.id.requestdetailofapplicant_li_needgone);
        tvInstead = (TextView) findViewById(R.id.requestdetailofapplicant_tv_instead);
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
    private void starAnimation() {
        if (isRequestShow) {
            btrPostApplication.setClickable(false);
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
            timer.schedule(timerTask, 300);
        } else {
            btrPostApplication.setClickable(true);
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
            timer.schedule(timerTask, 300);
        }
    }

    /**
     * 尝试报名
     */
    private void attempPostApplication() {
        String applicant = bill.getApplicant();
        if (applicant == null || applicant.equals("")) {
            bill.setApplicant(User.getInstance().getUserName());
        } else {
            bill.setApplicant(bill.getApplicant() + "=" + User.getInstance().getUserName());
        }
        BillUtils.changeApplicantor(handler, bill, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
