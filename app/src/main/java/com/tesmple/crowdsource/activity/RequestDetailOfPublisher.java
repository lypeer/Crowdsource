package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.ApplicantFragment;
import com.tesmple.crowdsource.fragment.BillCommentFragment;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.utils.ActivityCollector;

import java.util.ArrayList;
import java.util.List;

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
     * bill详情
     */
    private TextView tvDetail;

    /**
     * publisher看到的bill详情的目标bill
     */
    private Bill bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdetailofpublisher);
        initView();
        getBundle();
        initToolbar();
        initTabAndViewPager();
        setView();
    }

    private void initView(){
        sdvHeadPortrait = (SimpleDraweeView)findViewById(R.id.requestdetailofpublisher_sdv_head_portrait);
        tvName = (TextView)findViewById(R.id.requestdetailofpublisher_tv_name);
        tvSchool = (TextView)findViewById(R.id.requestdetailofpublisher_tv_school);
        tvDetail = (TextView)findViewById(R.id.requestdetailofpublisher_tv_detail);
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
        toolbar.setLogo(R.drawable.ic_back);
        setSupportActionBar(toolbar);
    }

    /**
     * 设置bill详情
     */
    private void setView(){
        sdvHeadPortrait.setImageURI(Uri.parse(bill.getPublisherHeadPortrait()));
        tvName.setText(bill.getPublisherName());
        tvSchool.setText(bill.getPublisherSchool());
        tvSchool.setText(bill.getDetail());
    }
}
