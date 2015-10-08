package com.tesmple.crowdsource.baseactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gc.materialdesign.views.ButtonFlat;
import com.tesmple.crowdsource.R;

/**
 * Created by ESIR on 2015/10/7.
 */
public class LoginActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolbar();

        ButtonFlat btnForgetPassword = (ButtonFlat)findViewById(R.id.btn_forgetpassword);

        //下面这句代码其实修改的是button的文字颜色
        btnForgetPassword.setBackgroundColor(getResources().getColor(R.color.colorGrey));
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("登陆账号");
    }
}
