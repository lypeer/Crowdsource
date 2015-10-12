package com.tesmple.crowdsource.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.ParseXMLUtils;
import com.tesmple.crowdsource.view.Button;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.tesmple.crowdsource.view.LoopListener;
import com.tesmple.crowdsource.view.LoopView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 存放了省份的id的list
     */
    private ArrayList<String> provinceIdList;

    /**
     * 存放了城市的名称的list
     */
    private ArrayList<String> cityList;

    /**
     * 省份的名字
     */
    private String provinceName;

    /**
     * 城市的名字
     */
    private String cityName;

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
    private void init() {
        btnChoosePlace = (ButtonFlat) findViewById(R.id.perfectinfo_btn_choose_place);
        tvPlace = (TextView) findViewById(R.id.perfectinfo_tv_place);
        btnChooseSchool = (ButtonFlat) findViewById(R.id.perfectinfo_btn_choose_school);
        tvSchool = (TextView) findViewById(R.id.perfectinfo_tv_school);
        etStuNum = (EditText) findViewById(R.id.perfectinfo_et_stu_num);
        etPassword = (EditText) findViewById(R.id.perfectinfo_et_password);
        btnSure = (ButtonRectangle) findViewById(R.id.perfectinfo_btn_sure);

        btnChoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlacePicker();
            }
        });
    }

    /**
     * 显示placepicker的方法
     */
    private void showPlacePicker() {
        provinceName = getString(R.string.beijin);
        cityName = getString(R.string.beijin);

        //对与省份相关的list的处理
        final ArrayList<String> provinceList = new ArrayList<>();
        provinceIdList = new ArrayList<>();
        List<Map<String, String>> provinceMapList = ParseXMLUtils.getProvincesList();
        for (Map<String, String> map : provinceMapList) {
            provinceList.add(map.get("name"));
            provinceIdList.add(map.get("id"));
        }

        //对与城市相关的list的处理
        cityList = ParseXMLUtils.getCitiesList(1 + "");

        //创建dialog对象
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.prompt_please_choose_place);

        //初始化
        final LinearLayout loopLayout = new LinearLayout(this);
        final LoopView provincesLoopView = new LoopView(this);
        final LoopView citiesLoopView = new LoopView(this);

        //外层linearlayout的params
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //两个滚轮的params
        LinearLayout.LayoutParams lovParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        loopLayout.setLayoutParams(llParams);
//        loopLayout.setWeightSum(2);
        loopLayout.setOrientation(LinearLayout.HORIZONTAL);
        provincesLoopView.setLayoutParams(lovParams);
        citiesLoopView.setLayoutParams(lovParams);

        citiesLoopView.setNotLoop();
        citiesLoopView.setArrayList(cityList);

        provincesLoopView.setNotLoop();
        provincesLoopView.setArrayList(provinceList);

        //点击城市的值的时候更改城市的值
        provincesLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                cityList = ParseXMLUtils.getCitiesList(provinceIdList.get(item));
                citiesLoopView.setArrayList(cityList);
                loopLayout.removeView(citiesLoopView);
                loopLayout.addView(citiesLoopView);
            }
        });
        //将两个滚轮加入linearlayout
        loopLayout.addView(provincesLoopView);
        loopLayout.addView(citiesLoopView);
        //将；inearlayout加入到dialog
        builder.setView(loopLayout);

        builder.setPositiveButton(R.string.prompt_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                provinceName = provinceList.get(provincesLoopView.getCurrentIndex());
                cityName = cityList.get(citiesLoopView.getCurrentIndex());
                if (cityList.size() != 1) {
                    tvPlace.setText(provinceName + cityName);
                } else {
                    tvPlace.setText(cityName);
                }
            }
        });
        builder.setNegativeButton(R.string.prompt_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
