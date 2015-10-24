package com.tesmple.crowdsource.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.BillUtils;
import com.tesmple.crowdsource.utils.ParseXMLUtils;
import com.tesmple.crowdsource.utils.StringUtils;
import com.tesmple.crowdsource.utils.VerifyStuNumUtils;
import com.tesmple.crowdsource.view.Button;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.tesmple.crowdsource.view.LoopListener;
import com.tesmple.crowdsource.view.LoopView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by lypeer on 10/11/2015.
 */
public class PerfectInformationActivity extends AppCompatActivity {

    /**
     * 输入用户的昵称的输入框
     */
    private EditText etNickname;

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

    /**
     * 表征dialog是否正在显示的布尔值
     */
    private boolean isShowing = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StringUtils.VERIFY_STUNUM_FAILED:
                    App.dismissDialog();
                    isShowing = false;
                    Snackbar.make(btnChoosePlace, R.string.error_invalid_stu_num, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    break;
                case StringUtils.VERIFY_STUNUM_SUCCESSFULLY:
                    Bundle bundle = msg.getData();
                    String fuckhtml = bundle.getString("fuckhtml");
                    String[] strings = fuckhtml.split(" ");
                    initUser(strings);
                    saveInfo();
                    break;
                case StringUtils.NETWORK_ERROE:
                    App.dismissDialog();
                    isShowing = false;
                    Snackbar.make(btnChoosePlace, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();


            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_information);
        ActivityCollector.addActivity(this);
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
        etNickname = (EditText) findViewById(R.id.perfectinfo_et_nickname);
        btnChoosePlace = (ButtonFlat) findViewById(R.id.perfectinfo_btn_choose_place);
        tvPlace = (TextView) findViewById(R.id.perfectinfo_tv_place);
        btnChooseSchool = (ButtonFlat) findViewById(R.id.perfectinfo_btn_choose_school);
        tvSchool = (TextView) findViewById(R.id.perfectinfo_tv_school);
        etStuNum = (EditText) findViewById(R.id.perfectinfo_et_stu_num);
        etPassword = (EditText) findViewById(R.id.perfectinfo_et_password);
        btnSure = (ButtonRectangle) findViewById(R.id.perfectinfo_btn_sure);

        etNickname.setError(null);
        etStuNum.setError(null);
        etPassword.setError(null);

        btnChoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //暂时将这个方法注释掉，这个版本只要默认电子科技大学
//                showPlacePicker();
            }
        });

        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etStuNum.getText().toString().trim().equals("")) {
                    etStuNum.setError(getString(R.string.error_please_input_stu_num));
                } else if (etPassword.getText().toString().trim().equals("")) {
                    etPassword.setError(getString(R.string.error_please_input_password));
                } else if (etNickname.getText().toString().trim().equals("")) {
                    etNickname.setError(getString(R.string.error_please_input_nickname));
                } else {
                    stuNumIsExist(etStuNum.getText().toString().trim());
                }
//                Intent intent = new Intent(PerfectInformationActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }

    /**
     * 初始化user的方法
     *
     * @param strings user的一些信息
     */
    private void initUser(String[] strings) {
        User.getInstance().setNickName(etNickname.getText().toString().trim());
        User.getInstance().setSchool(tvSchool.getText().toString().trim());
        User.getInstance().setStuNum(etStuNum.getText().toString().trim());
        User.getInstance().setName(strings[4]);
        User.getInstance().setDepartment(strings[22]);
        User.getInstance().setMajor(strings[24]);
        User.getInstance().setGender(strings[10]);
        if(User.getInstance().getGender().equals(getString(R.string.man))){
            User.getInstance().setHeadProtrait(Uri.
                    parse("android.resource://com.tesmple.crowdsource/drawable/default_head_portrait_boy").toString());
        }else {
            User.getInstance().setHeadProtrait(Uri.
                    parse("android.resource://com.tesmple.crowdsource/drawable/default_head_portrait_girl").toString());
        }
    }

    /**
     * 验证用户的学号是否已经注册的方法
     *
     * @param stuNum 需要验证的学号
     */
    private void stuNumIsExist(final String stuNum) {
        App.showDialog(PerfectInformationActivity.this);
        isShowing = true;
        AVQuery<AVObject> avQuery = new AVQuery<>("_User");
        avQuery.whereEqualTo("stu_num", stuNum);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        VerifyStuNumUtils.verifyStuNum(mHandler, tvSchool.getText().toString().trim(),
                                stuNum, etPassword.getText().toString().trim());
                    } else {
                        App.dismissDialog();
                        isShowing = false;
                        Snackbar.make(btnChoosePlace, R.string.error_stu_num_exist, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                } else {
                    Log.e("StuNumError", e.getMessage() + "===" + e.getCode());
                    App.dismissDialog();
                    isShowing = false;
                    Snackbar.make(btnChoosePlace, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
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

    /**
     * 保存用户的信息的方法
     */
    private void saveInfo() {
        AVUser avUser = AVUser.getCurrentUser();
        avUser.put("school", User.getInstance().getSchool());
        avUser.put("stu_num", User.getInstance().getStuNum());
        avUser.put("nickname", User.getInstance().getNickName());
        avUser.put("department", User.getInstance().getDepartment());
        avUser.put("name", User.getInstance().getName());
        avUser.put("gender", User.getInstance().getGender());
        avUser.put("major", User.getInstance().getMajor());
        Drawable drawable;
        String gender = User.getInstance().getGender();
        if (gender.equals(getString(R.string.man))) {
            drawable = App.getContext().getResources().getDrawable(R.drawable.default_head_portrait_boy);
        } else {
            drawable = App.getContext().getResources().getDrawable(R.drawable.default_head_portrait_girl);
        }
        Bitmap bitmap = (drawable) != null ? ((BitmapDrawable) drawable).getBitmap() : null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        AVFile file = new AVFile("icon.jpeg", stream.toByteArray());

        avUser.put("head_portrait", file);

        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    App.dismissDialog();
                    isShowing = false;
                    Intent intent = new Intent(PerfectInformationActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    App.dismissDialog();
                    isShowing = false;
                    Log.e("perfectInfoSaveError", e.getMessage() + "===" + e.getCode());
                    Snackbar.make(btnChoosePlace, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (!isShowing) {
            super.onBackPressed();
        }
    }
}
