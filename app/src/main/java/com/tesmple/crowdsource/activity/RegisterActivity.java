package com.tesmple.crowdsource.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.CheckBox;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.StringUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lypeer on 10/7/2015.
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * 输入手机号码的输入框
     */
    private EditText etPhone;

    /**
     * 输入验证码的输入框
     */
    private EditText etProveCode;

    /**
     * 获得验证码的按钮
     */
    private ButtonRectangle btnGetProveCode;

    /**
     * 表示同意用户协议的checkbox
     */
    private CheckBox cbAgreeAgreement;

    /**
     * 表示用户协议的文本
     */
    private TextView tvAgreement;

    /**
     * 确认注册的按钮
     */
    private ButtonRectangle btnRegister;

    /**
     * 计时器的对象
     */
    private Timer mTimer;
    /**
     * 倒计时的时间
     */
    private int countNum = 59;

    /**
     * handler对象用于处理异步请求
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case StringUtils.SEND_SUCCESSFULLY :
                    btnCountDown();
                    break;
                case StringUtils.COUNT_DOWN:
                    btnGetProveCode.setText(countNum + "");
                    countNum--;
                    if (countNum < 0) {
                        countNum = 59;
                        mTimer.cancel();
                        btnGetProveCode.setClickable(true);
                        btnGetProveCode.setText(App.getContext().getResources().getString(R.string.get_prove_code));
                        btnGetProveCode.setBackgroundResource(R.color.colorPrimary);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initToolBar();
        init();
    }

    /**
     * 初始化toolbar的方法
     */
    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.register);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }

    private void init() {
        etPhone = (EditText) findViewById(R.id.register_et_phone);
        etProveCode = (EditText) findViewById(R.id.register_et_prove_code);
        btnGetProveCode = (ButtonRectangle) findViewById(R.id.register_btn_get_prove_code);
        cbAgreeAgreement = (CheckBox) findViewById(R.id.register_cb_agree_agreement);
        tvAgreement = (TextView) findViewById(R.id.register_tv_agreement);
        btnRegister = (ButtonRectangle) findViewById(R.id.register_btn_register);

        cbAgreeAgreement.setChecked(true);

        btnGetProveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = etPhone.getText().toString().trim();
                if (isPhoneNumber(phoneNum)) {
                    Log.e("asd" , phoneNum + "");
                    isPhoneNumberExist(phoneNum);
                }
            }
        });
    }

    /**
     * 判断输入的号码是否手机号的方法
     *
     * @param number 输入的号码
     * @return 如果是手机号，就返回true，如果不是手机号，就返回false
     */
    private boolean isPhoneNumber(String number) {
        if (number.length() == StringUtils.PHONE_NUMBER_MAX_LENGTH) {
            Pattern p = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|" +
                    "(17[0,7,9])|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(number);
            if (!m.matches()) {
                Snackbar.make(btnRegister, R.string.input_right_phone, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
            return m.matches();
        } else {
            Snackbar.make(btnRegister, R.string.input_right_phone, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
            return false;
        }
    }

    /**
     * 验证用户输入的号码是否已经注册过
     *
     * @param phoneNum 用户所输入的手机号码
     */
    private void isPhoneNumberExist(final String phoneNum) {
        Log.e("qwe" , phoneNum + "");
        App.showDialog(RegisterActivity.this , R.string.sending);
        AVQuery<AVUser> query = AVUser.getQuery();

        query.whereEqualTo("username", phoneNum);

        query.findInBackground(new FindCallback<AVUser>() {

            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    if (list.get(0) != null) {
                        App.dismissDialog();
                        Snackbar.make(btnRegister, R.string.phone_has_registered, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                } else {
                    //错误码为0表示找不到当前用户
                    if (e.getCode() == 0) {
                        sendMessage(phoneNum);
                    } else {
                        Log.e("Register_isExist", e.getMessage() + "===" + e.getCode());
                         App.dismissDialog();
                        Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }

            }
        });
    }

    /**
     * 向指定手机号发送验证短信
     *
     * @param phoneNum 发送短信的手机号
     */
    private void sendMessage(String phoneNum) {
        AVOSCloud.requestSMSCodeInBackground(phoneNum,
                new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Message message = new Message();
                            message.what = StringUtils.SEND_SUCCESSFULLY;
                            mHandler.sendMessage(message);
                            App.dismissDialog();
                            Snackbar.make(btnRegister, R.string.send_successfully, Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        } else {
                            if (e.getCode() == 600) {
                                Snackbar.make(btnRegister, R.string.so_frequently, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else {
                                Log.e("Register_send", e.getMessage() + "===" + e.getCode());
                                Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                            App.dismissDialog();
                        }
                    }
                });
    }

    /**
     * 使按钮呈现倒计时的样子
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void btnCountDown() {
        btnGetProveCode.setFocusable(false);
        btnGetProveCode.setFocusableInTouchMode(false);
        btnGetProveCode.setPressed(false);
        btnGetProveCode.setClickable(false);
        btnGetProveCode.setBackgroundResource(R.drawable.btn_gray_pressed);

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = StringUtils.COUNT_DOWN;
                mHandler.sendMessage(message);
            }
        }, 1000, 1000);
    }
}
