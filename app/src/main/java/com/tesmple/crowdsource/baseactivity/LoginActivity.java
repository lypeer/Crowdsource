package com.tesmple.crowdsource.baseactivity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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
    }

    private void initToolbar(){
        Toolbar toolbar = (Toolbar)findViewById(R.id.login_toolbar);
        toolbar.setTitle("登陆账号");
    }
}
