package com.tesmple.crowdsource.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.object.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lypeer on 10/10/2015.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }

    /**
     * 初始化的方法，在里面完成初始化控件等操作
     */
    private void init() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                initUser();
            }
        }, 1500);
    }


    /**
     * 初始化user的属性
     */
    public void initUser() {
        if (AVUser.getCurrentUser() != null) {
            AVUser avuser = AVUser.getCurrentUser();
            avuser.refreshInBackground(new RefreshCallback<AVObject>() {
                @Override
                public void done(AVObject avObject, AVException e) {
                    if (e == null) {
                        User.getInstance().setName((String) avObject.get("name"));
                        User.getInstance().setUserName((String) avObject.get("username"));
                        User.getInstance().setStuNum((String) avObject.get("stu_num"));
                        User.getInstance().setSchool((String) avObject.get("school"));
                        User.getInstance().setGender((String) avObject.get("gender"));
                        User.getInstance().setCreditValue((String) avObject.get("credit_value"));
                        User.getInstance().setSendStar((String) avObject.get("send_star"));
                        User.getInstance().setAcceptStar((String) avObject.get("accept_star"));
                        User.getInstance().setStatus((String) avObject.get("status"));
                        User.getInstance().setNickName((String) avObject.get("nickname"));
                        User.getInstance().setDepartment((String) avObject.get("department"));
                        User.getInstance().setMajor((String) avObject.get("major"));

                        if (avObject.get("nickname") != null) {
                            User.getInstance().setHeadProtrait(avObject.getAVFile("head_portrait").getUrl());

                            if (!avObject.get("installationId").equals(AVInstallation.getCurrentInstallation().getInstallationId())) {
                                avObject.put("installationId", AVInstallation.getCurrentInstallation().getInstallationId());
                                avObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) {
                                        if (e == null) {
                                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.e("AppSaveError", e.getMessage() + "===" + e.getCode());
                                            Snackbar.make(findViewById(R.id.iv_welcome), R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                                    .setAction("Action", null).show();
                                            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });
                            } else {
                                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Intent intent = new Intent(WelcomeActivity.this, PerfectInformationActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Log.e("AppError", e.getMessage() + "===" + e.getCode());
                        Snackbar.make(findViewById(R.id.iv_welcome), R.string.please_check_your_network, Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
