package com.tesmple.crowdsource.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.tesmple.crowdsource.R;

import im.fir.sdk.FIR;

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
        FIR.init(this);
        super.onCreate();
        mAppContext = getApplicationContext();
        AVOSCloud.initialize(this, "ToU9po43RDw6nyqcjzPL57si", "GiI6qViVwvAsCpz46SjLarm2");
        Fresco.initialize(mAppContext);
        PushService.setDefaultPushCallback(mAppContext, NotificationActivity.class);
        PushService.subscribe(mAppContext, "public", NotificationActivity.class);

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
        llBackground.setAlpha(0.8f);
        llProgressbar.bringToFront();
        llProgressbar.setVisibility(View.VISIBLE);


    }

//    /**
//     * 显示正在加载的dialog
//     *
//     * @param context 发起带dialog的activity的activity
//     *                 @param fragment 想要dialog的fragment
//     */
//    public static void showDialog(Context context , String fragment) {
//        switch (fragment){
//            case StringUtils.FRAGMENT_ACCEPTABLE_BILL:
//                proBarProgressAcceptableBill = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.proBar_progress);
//                llProgressbarAcceptableBill = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);
//                llBackgroundAcceptableBill = (LinearLayout) rootView.findViewById(R.id.ll_background);
//
//                rootView.findViewById(R.id.rl_dialog).bringToFront();
//                llBackgroundAcceptableBill.bringToFront();
//                llBackgroundAcceptableBill.setAlpha(0.8f);
//                llProgressbarAcceptableBill.bringToFront();
//                llProgressbarAcceptableBill.setVisibility(View.VISIBLE);
//                break;
//            case StringUtils.FRAGMENT_ACCEPTED_BILL:
//                proBarProgressAcceptedBill = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.proBar_progress);
//                llProgressbarAcceptedBill = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);
//                llBackgroundAcceptedBill = (LinearLayout) rootView.findViewById(R.id.ll_background);
//
//                rootView.findViewById(R.id.rl_dialog).bringToFront();
//                llBackgroundAcceptedBill.bringToFront();
//                llBackgroundAcceptedBill.setAlpha(0.8f);
//                llProgressbarAcceptedBill.bringToFront();
//                llProgressbarAcceptedBill.setVisibility(View.VISIBLE);
//                break;
//            case StringUtils.FRAGMENT_MY_PUBLISH:
//                proBarProgressMyPublish = (ProgressBarCircularIndeterminate) rootView.findViewById(R.id.proBar_progress);
//                llProgressbarMyPublish = (LinearLayout) rootView.findViewById(R.id.ll_progressbar);
//                llBackgroundMyPublish = (LinearLayout) rootView.findViewById(R.id.ll_background);
//
//                rootView.findViewById(R.id.rl_dialog).bringToFront();
//                llBackgroundMyPublish.bringToFront();
//                llBackgroundMyPublish.setAlpha(0.8f);
//                llProgressbarMyPublish.bringToFront();
//                llProgressbarMyPublish.setVisibility(View.VISIBLE);
//                break;
//        }
//        android.support.v7.app.AlertDialog.Builder builder =  new android.support.v7.app.AlertDialog.Builder(context);
//        builder.setMessage(R.string.prompt_waiting);
//        builder.show();
//    }

    /**
     * 隐藏正在加载的dialog
     */
    public static void dismissDialog() {
        llBackground.setAlpha(0.0f);
//        llBackground.setVisibility(View.GONE);
        llProgressbar.setVisibility(View.GONE);
    }

//    /**
//     * 隐藏正在加载的dialog
//     * @param fragment 想要隐藏的fragment的dialog
//     */
//    public static void dismissDialog(String fragment) {
//        switch (fragment){
//            case StringUtils.FRAGMENT_ACCEPTABLE_BILL:
//                llBackgroundAcceptableBill.setVisibility(View.GONE);
//                llProgressbarAcceptableBill.setVisibility(View.GONE);
//                break;
//            case StringUtils.FRAGMENT_ACCEPTED_BILL:
//                llBackgroundAcceptedBill.setVisibility(View.GONE);
//                llProgressbarAcceptedBill.setVisibility(View.GONE);
//                break;
//            case StringUtils.FRAGMENT_MY_PUBLISH:
//                llBackgroundMyPublish.setVisibility(View.GONE);
//                llProgressbarMyPublish.setVisibility(View.GONE);
//                break;
//        }

//    }
}
