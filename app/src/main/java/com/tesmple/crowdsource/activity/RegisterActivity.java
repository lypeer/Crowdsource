package com.tesmple.crowdsource.activity;

import android.content.Intent;
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
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.gc.materialdesign.views.CheckBox;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.view.ButtonRectangle;
import com.tesmple.crowdsource.utils.EditTextUtils;
import com.tesmple.crowdsource.utils.StringUtils;

import org.json.JSONArray;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lypeer on 10/7/2015.
 */
public class RegisterActivity extends AppCompatActivity {

    /**
     * 输入手机号码的输入框
     */
    private EditText etPhone;

    /**
     * 输入密码的输入框
     */
    private EditText etPassword;

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
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StringUtils.SEND_SUCCESSFULLY:
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
                        btnGetProveCode.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityCollector.addActivity(this);
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
        etPassword = (EditText) findViewById(R.id.register_et_password);
        etProveCode = (EditText) findViewById(R.id.register_et_prove_code);
        btnGetProveCode = (ButtonRectangle) findViewById(R.id.register_btn_get_prove_code);
        cbAgreeAgreement = (CheckBox) findViewById(R.id.register_cb_agree_agreement);
        tvAgreement = (TextView) findViewById(R.id.register_tv_agreement);
        btnRegister = (ButtonRectangle) findViewById(R.id.register_btn_register);

        etPhone.setError(null);
        etPassword.setError(null);
        etProveCode.setError(null);

        cbAgreeAgreement.post(new Runnable() {
            @Override
            public void run() {
                cbAgreeAgreement.setChecked(true);
            }
        });

        btnGetProveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputCorrectly()) {
                    isPhoneNumberExist(etPhone.getText().toString().trim());

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputCorrectly()) {
                    String proveCode = etProveCode.getText().toString().trim();
                    if (!EditTextUtils.isProveCode(proveCode)) {
                        etProveCode.setError(getString(R.string.error_prove_code_should_be));
                    } else if (!cbAgreeAgreement.isCheck()) {
                        Snackbar.make(btnRegister, R.string.error_please_check_agreement, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        verifyProveCode(proveCode);
                    }
                }
//                Intent intent = new Intent(RegisterActivity.this, PerfectInformationActivity.class);
//                startActivity(intent);
            }
        });
    }

    /**
     * 验证用户输入的手机号和密码是否符合规定
     *
     * @return 如果符合就返回true，如果不符合就返回false
     */
    private boolean isInputCorrectly() {
        String phoneNum = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (!EditTextUtils.isPhoneNumber(phoneNum)) {
            etPhone.setError(getString(R.string.error_invalid_phone));
            return false;
        } else if (!EditTextUtils.isPassword(password)) {
            etPassword.setError(getString(R.string.error_password_should_be));
            return false;
        } else {
            return true;
        }
    }

    /**
     * 验证用户输入的验证码和手机号是否符合的方法
     *
     * @param proveCode 用户输入的验证码
     */
    private void verifyProveCode(String proveCode) {
        AVOSCloud.verifyCodeInBackground(proveCode, etPhone.getText().toString().trim(), new AVMobilePhoneVerifyCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    AVUser avUser = new AVUser();
                    avUser.put("username", etPhone.getText().toString().trim());
                    avUser.put("password", etPassword.getText().toString().trim());
                    avUser.put("installationId", AVInstallation.getCurrentInstallation().getInstallationId());
                    avUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                User.getInstance().setUserName(etPhone.getText().toString().trim());
                                AVObject avObject = new AVObject("UserHelper");
                                avObject.put("username", User.getInstance().getUserName());
                                avObject.put("notification", new JSONArray());
                                avObject.put("user_comment", new JSONArray());
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            Intent intent = new Intent(RegisterActivity.this, PerfectInformationActivity.class);
                                            startActivity(intent);
                                        }else {
                                            Log.e("RegisterHelperSaveError", e.getMessage() + "===" + e.getCode());
                                            Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                                    .setAction("Action", null).show();
                                        }
                                    }
                                });

                            } else {
                                Log.e("RegisterVerifyError", e.getMessage() + "===" + e.getCode());
                                Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }
                        }
                    });

                } else {
                    if (e.getCode() == 603) {
                        Snackbar.make(btnRegister, R.string.error_invalid_provr_code, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        Log.e("RegisterVerifyError", e.getMessage() + "===" + e.getCode());
                        Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }
            }
        });
    }


    /**
     * 验证用户输入的号码是否已经注册过
     *
     * @param phoneNum 用户所输入的手机号码
     */
    private void isPhoneNumberExist(final String phoneNum) {
        App.showDialog(RegisterActivity.this);

        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("username", phoneNum);
        query.findInBackground(new FindCallback<AVUser>() {

            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        App.dismissDialog();
                        Snackbar.make(btnRegister, R.string.phone_has_registered, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        sendMessage(phoneNum);
                    }
                } else {
                    Log.e("Register_isExist", e.getMessage() + "===" + e.getCode());
                    App.dismissDialog();
                    Snackbar.make(btnRegister, R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();

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
        App.showDialog(RegisterActivity.this);
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
                            if (e.getCode() == 1) {
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
    private void btnCountDown() {
        btnGetProveCode.setFocusable(false);
        btnGetProveCode.setFocusableInTouchMode(false);
        btnGetProveCode.setPressed(false);
        btnGetProveCode.setClickable(false);
        btnGetProveCode.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorGrey));

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
