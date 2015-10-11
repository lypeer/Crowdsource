package com.tesmple.crowdsource.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVOSCloud;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.tesmple.crowdsource.R;

import dmax.dialog.SpotsDialog;

/**
 * Created by lypeer on 10/7/2015.
 */
public class App extends Application {
    /**
     * 全局context对象
     */
    private static Context mAppContext;

    /**
     * 进度条外部layout
     */
    private static LinearLayout llProgressbar;

    /**
     * 进度条外部layout底下的那个linearlayout
     */
    private static LinearLayout llBackground;

    /**
     * 加载progressBar
     */
    private static ProgressBarCircularIndeterminate proBarProgress;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        AVOSCloud.initialize(this, "ToU9po43RDw6nyqcjzPL57si", "GiI6qViVwvAsCpz46SjLarm2");
    }

    /**
     * 获得全局context的方法
     *
     * @return 返回全局context
     */
    public static Context getContext() {
        return mAppContext;
    }

    /**
     * 显示正在加载的dialog
     *
     * @param activity 发起带dialog的activity的activity
     */
    public static void showDialog(Activity activity) {
        proBarProgress = (ProgressBarCircularIndeterminate) activity.findViewById(R.id.proBar_progress);
        llProgressbar = (LinearLayout) activity.findViewById(R.id.ll_progressbar);
        llBackground = (LinearLayout) activity.findViewById(R.id.ll_background);

        activity.findViewById(R.id.rl_dialog).bringToFront();
        llBackground.bringToFront();
        llBackground.setAlpha(0.5f);
        llProgressbar.bringToFront();
        llProgressbar.setVisibility(View.VISIBLE);


    }

    /**
     * 隐藏正在加载的dialog
     */
    public static void dismissDialog() {
        llBackground.setVisibility(View.GONE);
        llProgressbar.setVisibility(View.GONE);
    }
}
