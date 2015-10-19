package com.tesmple.crowdsource.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.adapter.ViewPagerAdapter;
import com.tesmple.crowdsource.fragment.AcceptableBillFragment;
import com.tesmple.crowdsource.fragment.ApplicantFragment;
import com.tesmple.crowdsource.fragment.CommentFragment;
import com.tesmple.crowdsource.fragment.MyPublishFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestdetailofpublisher);
        ActivityCollector.addActivity(this);
        ActivityCollector.finishAllExceptNow(this);

        initToolbar();
        initTabAndViewPager();
    }


    private void initTabAndViewPager(){
        tlCompliantAndComment = (TabLayout)findViewById(R.id.requestdetailofpublisher_tl_compliantandcomment);
        vpCompliantAndComment = (ViewPager)findViewById(R.id.requestdetailofpublisher_vp_compliantandcomment);
        initList();

        vpCompliantAndComment.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList));
        tlCompliantAndComment.setupWithViewPager(vpCompliantAndComment);
        tlCompliantAndComment.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initList(){
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();

        titleList.add("报名者");
        titleList.add("评论");

        fragmentList.add(new ApplicantFragment());
        fragmentList.add(new CommentFragment());
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
}
