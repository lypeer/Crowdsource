package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.layout.Button;
import com.tesmple.crowdsource.layout.ButtonRectangle;

/**
 * Created by lypeer on 10/11/2015.
 */
public class PerfectInformationActivity extends AppCompatActivity {

    /**
     * 选择用户所在地区的按钮
     */
    private ButtonFlat btnChoosePlace;

    /**
     * 显示用户所在地区的textview
     */
    private TextView tvPlace;

    /**
     * 选择学校的按钮
     */
    private ButtonFlat btnChooseSchool;

    /**
     * 显示用户所在学校的按钮
     */
    private TextView tvSchool;

    /**
     * 输入用户的学号的edittext
     */
    private EditText etStuNum;

    /**
     * 输入用户的密码的edittext
     */
    private EditText etPassword;

    /**
     * 用户确认资料保存的按钮
     */
    private Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_information);
        initToolBar();
        init();
    }

    /**
     * 初始化toolbar的方法
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.perfect_information);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    /**
     * 初始化控件的方法
     */
    private void init(){
        btnChoosePlace = (ButtonFlat)findViewById(R.id.perfectinfo_btn_choose_place);
        tvPlace = (TextView)findViewById(R.id.perfectinfo_tv_place);
        btnChooseSchool = (ButtonFlat)findViewById(R.id.perfectinfo_btn_choose_school);
        tvSchool = (TextView)findViewById(R.id.perfectinfo_tv_school);
        etStuNum = (EditText)findViewById(R.id.perfectinfo_et_stu_num);
        etPassword = (EditText)findViewById(R.id.perfectinfo_et_password);
        btnSure = (ButtonRectangle)findViewById(R.id.perfectinfo_btn_sure);

        btnChoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
