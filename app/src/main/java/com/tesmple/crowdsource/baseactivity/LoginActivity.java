package com.tesmple.crowdsource.baseactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.ButtonRectangle;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.utils.ButtonUtils;
import com.tesmple.crowdsource.utils.EditTextUtils;

/**
 * Created by ESIR on 2015/10/7.
 */
public class LoginActivity extends Activity{
    /**
     *login界面的账户输入EditText
     */
    AutoCompleteTextView loginEtPhone;

    /**
     * login界面的密码输入EditText
     */
    AutoCompleteTextView loginEtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar();
        setButtons();
        setEditTexts();
    }

    /**
     * 设置login界面的button
     */
    private void setButtons(){
        ButtonFlat loginBtnForgetPassword = (ButtonFlat)findViewById(R.id.login_btn_forgetpassword);
        ButtonFlat loginBtnRegisterAccount = (ButtonFlat)findViewById(R.id.login_btn_registeraccount);
        ButtonRectangle loginBtnLogin = (ButtonRectangle)findViewById(R.id.login_btn_login);

        ButtonUtils.setBtnFlatTextColor(loginBtnForgetPassword, getResources().getColor(R.color.colorGrey));
        ButtonUtils.setBtnFlatTextColor(loginBtnRegisterAccount, getResources().getColor(R.color.colorPrimaryDark));
        loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempLogin();
            }
        });
    }

    /**
     * 设置login界面的EditText
     */
    private void setEditTexts(){
        loginEtPhone = (AutoCompleteTextView)findViewById(R.id.login_et_phone);
        loginEtPassword = (AutoCompleteTextView)findViewById(R.id.login_et_password);
        loginEtPhone.setError(null);
        loginEtPassword.setError(null);
        loginEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1){
                    int length = s.toString().length();
                    if (length == 3 || length == 8){
                        loginEtPhone.setText(s + " ");
                        loginEtPhone.setSelection(loginEtPhone.getText().toString().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 尝试登陆函数，进行输入差错判断
     */
    private void attempLogin(){

        String userPhone = loginEtPhone.getText().toString();
        String userPassword = loginEtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if( !(EditTextUtils.isPassword(userPassword)) ){
            loginEtPassword.setError(getString(R.string.error_invalid_password));
            cancel = !(EditTextUtils.isPassword(userPassword));
        }else if( !(EditTextUtils.isPhoneNumber(userPhone)) ){
            loginEtPhone.setError(getString(R.string.error_invalid_phone));
            cancel = !(EditTextUtils.isPhoneNumber(userPhone));
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //// TODO: 2015/10/8
            //AVUer设置
            /*AVUser.logInInBackground("username", "password", new LogInCallback() {
                public void done(AVUser user, AVException e) {
                    if (user != null) {
                        // 登录成功
                    } else {
                        // 登录失败
                    }
                }
            });*/
        }

    }

    /**
     * 初始化toolbar
     */
    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("登陆账号");
    }

    /**
     * 添加TextWather实现phone的自动添加空格
     */
    /*private final TextWatcher etPhoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            DLog.d("----------beforeTextChanged----------\n");
            DLog.d("s:" + s + "\n");
            DLog.d("start:" + start + "\n");
            DLog.d("count:" + count + "\n");
            DLog.d("after:" + after + "\n");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            DLog.d("----------onTextChanged----------\n");
            DLog.d("s:" + s + "\n");
            DLog.d("start:" + start + "\n");
            DLog.d("before:" + before + "\n");
            DLog.d("count:" + count + "\n");
        }

        @Override
        public void afterTextChanged(Editable s) {
            DLog.d("----------afterTextChanged----------\n");
            DLog.d("s:" + s + "\n");
        }
    }*/
}
