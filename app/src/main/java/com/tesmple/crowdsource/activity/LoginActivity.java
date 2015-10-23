package com.tesmple.crowdsource.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.User;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.ButtonUtils;
import com.tesmple.crowdsource.utils.EditTextUtils;

/**
 * Created by ESIR on 2015/10/7.
 */
public class LoginActivity extends AppCompatActivity{
    /**
     *login界面的账户输入EditText
     */
    private AutoCompleteTextView loginEtPhone;

    /**
     * login界面的密码输入EditText
     */
    private AutoCompleteTextView loginEtPassword;

    /**
     * login界面的主区域外部ScrollView
     */
    private ScrollView loginSvScrollForm;

    /**
     * login界面的加载progressBar
     */
    private ProgressBarCircularIndeterminate loginProBarProgress;

    /**
     * login界面的登陆按钮
     */
    private ButtonRectangle loginBtnLogin;

    /**
     * login界面的进度条外部layout
     */
    private LinearLayout loginLlProgressbar;

    /**
     * login界面的忘记密码按钮
     */
    private ButtonFlat loginBtnForgetpasswor;

    /**
     * login界面的注册账号按钮
     */
    private ButtonFlat loginBtnRegisteraccount;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityCollector.addActivity(LoginActivity.this);
        ActivityCollector.finishAllExceptNow(LoginActivity.this);
        initViewBind();
        initToolbar();
        setButtons();
        setEditTexts();
    }

    /**
     * 对控件绑定
     */
    private void initViewBind(){
        loginEtPhone = (AutoCompleteTextView)findViewById(R.id.login_et_phone);
        loginEtPassword = (AutoCompleteTextView)findViewById(R.id.login_et_password);
        loginSvScrollForm = (ScrollView)findViewById(R.id.login_sv_scrollform);
        loginProBarProgress = (ProgressBarCircularIndeterminate)findViewById(R.id.login_proBar_progress);
        loginBtnLogin = (ButtonRectangle)findViewById(R.id.login_btn_login);
        loginLlProgressbar = (LinearLayout)findViewById(R.id.login_ll_progressbar);
        loginBtnForgetpasswor = (ButtonFlat)findViewById(R.id.login_btn_forgetpassword);
        loginBtnRegisteraccount = (ButtonFlat)findViewById(R.id.login_btn_registeraccount);
    }

    /**
     * 设置login界面的button
     */
    private void setButtons(){
        ButtonFlat loginBtnForgetPassword = (ButtonFlat)findViewById(R.id.login_btn_forgetpassword);
        ButtonFlat loginBtnRegisterAccount = (ButtonFlat)findViewById(R.id.login_btn_registeraccount);

        ButtonUtils.setBtnFlatTextColor(loginBtnForgetPassword, getResources().getColor(R.color.colorGrey));
        ButtonUtils.setBtnFlatTextColor(loginBtnRegisterAccount, getResources().getColor(R.color.colorPrimaryDark));
        loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempLogin();
            }
        });

        loginBtnForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        loginBtnRegisterAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置login界面的EditText报错机制
     */
    private void setEditTexts(){
        loginEtPhone.setError(null);
        loginEtPassword.setError(null);
    }

    /**
     * 尝试登陆函数，进行输入差错判断
     */
    private void attempLogin(){

        String userPhone = loginEtPhone.getText().toString();
        String userPassword = loginEtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if( !(EditTextUtils.isPhoneNumber(userPhone)) ){
            loginEtPhone.setError(getString(R.string.error_invalid_phone));
            cancel = !(EditTextUtils.isPhoneNumber(userPhone));
            focusView = loginEtPhone;
        } else if( !(EditTextUtils.isPassword(userPassword)) ){
            loginEtPassword.setError(getString(R.string.error_invalid_password));
            cancel = !(EditTextUtils.isPassword(userPassword));
            focusView = loginEtPassword;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            /*Intent intent = new Intent(LoginActivity.this , PostRequestActivity.class);
            startActivity(intent);*/
            //loginSvScrollForm.setVisibility(View.GONE);
            loginSvScrollForm.setAlpha(0.5f);
            loginLlProgressbar.setVisibility(View.VISIBLE);
            AVUser.logInInBackground(userPhone , userPassword , new LogInCallback<AVUser>() {
                public void done(AVUser user, AVException e) {
                    if (e == null) {
                        if (user != null) {
                            User.getInstance().setName((String)user.get("name"));
                            User.getInstance().setUserName((String) user.get("username"));
                            User.getInstance().setStuNum((String) user.get("stu_num"));
                            User.getInstance().setSchool((String) user.get("school"));
                            User.getInstance().setGender((String) user.get("gender"));
                            User.getInstance().setHeadProtrait(user.getAVFile("head_portrait").getUrl());
                            User.getInstance().setCreditValue((String) user.get("credit_value"));
                            User.getInstance().setSendStar((String) user.get("send_star"));
                            User.getInstance().setAcceptStar((String) user.get("accept_star"));
                            User.getInstance().setStatus((String) user.get("status"));
                            User.getInstance().setNickName((String) user.get("nickname"));
                            User.getInstance().setDepartment((String) user.get("department"));
                            User.getInstance().setMajor((String) user.get("major"));

                            if (!user.get("installationId").equals(AVInstallation.getCurrentInstallation().getInstallationId())) {
                                user.put("installationId", AVInstallation.getCurrentInstallation().getInstallationId());
                                user.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Log.e("AppSaveError", e.getMessage() + "===" + e.getCode());
                                            Snackbar.make(loginEtPassword, R.string.error_phone_not_register,Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }else if(e.getCode() == 211){
                        Snackbar.make(loginEtPassword, R.string.error_phone_not_register,Snackbar.LENGTH_LONG).show();
                    }else if(e.getCode() == 210){
                        Snackbar.make(loginEtPassword,R.string.error_invalid_password,Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(loginEtPassword,R.string.please_check_your_network,Snackbar.LENGTH_LONG).show();
                    }
                    loginLlProgressbar.setVisibility(View.GONE);
                    loginSvScrollForm.setAlpha(1.0f);
                    //211 账号没注册
                    //210 账号或密码错误
                    //0 网络错误
                    //其他问题 网络错误
                }
            });
        }
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar(){
        Toolbar loginToolbar = (Toolbar)findViewById(R.id.toolbar);
        loginToolbar.setTitle("登陆账号");
        setSupportActionBar(loginToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(LoginActivity.this);
    }
}
