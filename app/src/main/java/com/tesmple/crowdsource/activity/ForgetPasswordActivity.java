package com.tesmple.crowdsource.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.gc.materialdesign.views.ButtonRectangle;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.ActivityCollector;
import com.tesmple.crowdsource.utils.EditTextUtils;
import com.tesmple.crowdsource.utils.StringUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ESIR on 2015/10/9.
 */
public class ForgetPasswordActivity extends AppCompatActivity{
    /**
     * 忘记密码界面的账号EditText
     */
    private AutoCompleteTextView forgetpasswordEtphone;

    /**
     * 忘记密码界面的新密码EditText
     */
    private AutoCompleteTextView forgetpasswordEtNewpassword;

    /**
     * 忘记密码界面的确认新密码EditText
     */
    private AutoCompleteTextView forgetpasswordEtConfirmpassword;

    /**
     * 忘记密码界面的验证码输入EditText
     */
    private AutoCompleteTextView forgetpasswordEtProvecode;

    /**
     * 忘记密码界面的获取验证码按钮
     */
    private com.tesmple.crowdsource.view.ButtonRectangle forgetpasswordBtnGetProveCode;

    /**
     * 忘记密码界面的保存按钮
     */
    private ButtonRectangle forgetpasswordBtnSavePassword;

    /**
     * 忘记密码界面的主界面外部的ScrollView
     */
    private ScrollView forgetpasswordSvScrollform;

    /**
     * 忘记密码界面的Progressbar外部布局
     */
    private LinearLayout forgetpasswordLlProgressbar;

    /**
     * 计时器的对象
     */
    private Timer mTimer;

    /**
     * 倒计时的时间
     */
    private int countNum = 59;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StringUtils.SEND_SUCCESSFULLY:
                    btnCountDown();
                    break;
                case StringUtils.COUNT_DOWN:
                    forgetpasswordBtnGetProveCode.setText(countNum + "");
                    countNum--;
                    if (countNum < 0) {
                        countNum = 59;
                        mTimer.cancel();
                        forgetpasswordBtnGetProveCode.setClickable(true);
                        forgetpasswordBtnGetProveCode.setText(App.getContext().getResources().getString(R.string.get_prove_code));
                        forgetpasswordBtnGetProveCode.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorPrimary));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);
        ActivityCollector.addActivity(this);
        initViewBind();
        initToolbar();
        setAutoCompleteTextViews();
        setButton();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar(){
        Toolbar forgetPasswordToolbar = (Toolbar)findViewById(R.id.toolbar);
        forgetPasswordToolbar.setTitle("修改密码");
        setSupportActionBar(forgetPasswordToolbar);
        forgetPasswordToolbar.setLogo(R.drawable.ic_back);
    }

    /**
     * 控件绑定
     */
    private void initViewBind(){
        forgetpasswordEtphone = (AutoCompleteTextView)findViewById(R.id.forgetpassword_et_phone);
        forgetpasswordEtNewpassword = (AutoCompleteTextView)findViewById(R.id.forgetpassword_et_newpassword);
        forgetpasswordEtConfirmpassword = (AutoCompleteTextView)findViewById(R.id.forgetpassword_et_confirmpassword);
        forgetpasswordEtProvecode = (AutoCompleteTextView)findViewById(R.id.forgetpassword_et_provecode);

        forgetpasswordBtnGetProveCode = (com.tesmple.crowdsource.view.ButtonRectangle)findViewById(R.id.forgetpassword_btn_getprovecode);
        forgetpasswordBtnSavePassword = (ButtonRectangle)findViewById(R.id.forgetpassword_btn_savepassword);

        forgetpasswordSvScrollform = (ScrollView)findViewById(R.id.forgetpassword_sv_scrollform);
        forgetpasswordLlProgressbar = (LinearLayout)findViewById(R.id.forgetpassword_ll_progressbar);
    }

    /**
     * 设置忘记密码界面的AutoCompleteTextView报错机制
     */
    private void setAutoCompleteTextViews(){
        forgetpasswordEtphone.setError(null);
        forgetpasswordEtNewpassword.setError(null);
        forgetpasswordEtConfirmpassword.setError(null);
        forgetpasswordEtProvecode.setError(null);
    }

    /**
     * 设置button相关监听
     */
    private void setButton(){
        forgetpasswordBtnGetProveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempGetProveCode();
            }
        });

        forgetpasswordBtnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempSavePassword();
            }
        });
    }

    /**
     * 尝试获取验证码
     */
    private void attempGetProveCode(){
        String userPhone = forgetpasswordEtphone.getText().toString().trim();
        String userNewPassword = forgetpasswordEtNewpassword.getText().toString();
        String userConfirmPassword = forgetpasswordEtConfirmpassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if( !EditTextUtils.isPhoneNumber(userPhone) ){
            forgetpasswordEtphone.setError(getString(R.string.error_invalid_phone));
            cancel = true;
            focusView = forgetpasswordEtphone;
        }else if( !EditTextUtils.isPassword(userNewPassword)){
            forgetpasswordEtNewpassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = forgetpasswordEtNewpassword;
        }else if( !userNewPassword.equals(userConfirmPassword) ){
            forgetpasswordEtNewpassword.setError(getString(R.string.error_newpassword_notsame));
            cancel = true;
            focusView = forgetpasswordEtNewpassword;
        }

        if(cancel){
            focusView.requestFocus();
        }else {
            forgetpasswordSvScrollform.setAlpha(0.5f);
            forgetpasswordEtProvecode.requestFocus();
            forgetpasswordLlProgressbar.setVisibility(View.VISIBLE);
            Log.i("userPhone", forgetpasswordEtphone.getText().toString().trim());
            AVUser.requestPasswordResetBySmsCodeInBackground(userPhone, new RequestMobileCodeCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        //发送成功了
                    } else if(e.getCode() == 211){
                        Snackbar.make(forgetpasswordBtnSavePassword, R.string.error_phone_not_register, Snackbar.LENGTH_LONG).show();
                    } else if(e.getCode() == AVException.USER_WITH_MOBILEPHONE_NOT_FOUND){
                        Snackbar.make(forgetpasswordBtnSavePassword, R.string.error_phone_not_register,Snackbar.LENGTH_LONG).show();
                    } else if(e.getCode() == 600 || e.getCode() == 1){
                        Snackbar.make(forgetpasswordBtnSavePassword, R.string.so_frequently, Snackbar.LENGTH_LONG).show();
                    } else if(e.getCode() == 216){
                        Snackbar.make(forgetpasswordBtnSavePassword, R.string.error_phonenotprove,Snackbar.LENGTH_LONG).show();
                    } else  {
                        Snackbar.make(forgetpasswordBtnSavePassword,R.string.please_check_your_network,Snackbar.LENGTH_LONG).show();
                    }
                    Log.i("getProveCodeError", String.valueOf(e.getCode() + "..." + e.getMessage()));
                    forgetpasswordSvScrollform.setAlpha(1.0f);
                    forgetpasswordLlProgressbar.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 尝试保存新密码
     */
    private void attempSavePassword(){
        String userPhone = forgetpasswordEtphone.getText().toString();
        String userNewPassword = forgetpasswordEtNewpassword.getText().toString();
        String userConfirmPassword = forgetpasswordEtConfirmpassword.getText().toString();
        String proveCode = forgetpasswordEtProvecode.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if( !EditTextUtils.isPhoneNumber(userPhone) ){
            forgetpasswordEtphone.setError(getString(R.string.error_invalid_phone));
            cancel = true;
            focusView = forgetpasswordEtphone;
        }else if( !EditTextUtils.isPassword(userNewPassword)){
            forgetpasswordEtNewpassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = forgetpasswordEtNewpassword;
        }else if( !userNewPassword.equals(userConfirmPassword) ){
            forgetpasswordEtNewpassword.setError(getString(R.string.error_newpassword_notsame));
            cancel = true;
            focusView = forgetpasswordEtNewpassword;
        }else if( !EditTextUtils.isProveCode(proveCode) ){
            forgetpasswordEtProvecode.setError(getString(R.string.error_provecode));
            cancel = true;
            focusView = forgetpasswordEtProvecode;
        }

        if(cancel){
            focusView.requestFocus();
        }else {
         AVUser.resetPasswordBySmsCodeInBackground(proveCode, userNewPassword, new UpdatePasswordCallback() {
             @Override
             public void done(AVException e) {
                 if (e == null) {
                     finish();
                 } else {
                     Log.i("ecode", String.valueOf(e.getCode()));
                 }
             }
         });
        }
    }

    /**
     * 使按钮呈现倒计时的样子
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void btnCountDown() {
        forgetpasswordBtnGetProveCode.setFocusable(false);
        forgetpasswordBtnGetProveCode.setFocusableInTouchMode(false);
        forgetpasswordBtnGetProveCode.setPressed(false);
        forgetpasswordBtnGetProveCode.setClickable(false);
        forgetpasswordBtnGetProveCode.setBackgroundColor(App.getContext().getResources().getColor(R.color.colorGrey));

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
