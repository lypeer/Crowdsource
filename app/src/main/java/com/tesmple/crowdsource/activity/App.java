package com.tesmple.crowdsource.activity;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVOSCloud;

import dmax.dialog.SpotsDialog;

/**
 *
 * Created by lypeer on 10/7/2015.
 */
public class App extends Application {
    /**
     * 全局context对象
     */
    private static Context mAppContext;

    /**
     * 在加载数据或者是发送请求的时候弹出的对话框
     */
    private static AlertDialog mAlertDialog;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
        AVOSCloud.initialize(this , "ToU9po43RDw6nyqcjzPL57si" , "GiI6qViVwvAsCpz46SjLarm2");
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
     * @param context 发起带dialog的activity的context
     * @param titleRes 显示的dialog的title值
     */
    public static void showDialog(Context context , int titleRes){
//        Log.e("aaa" , mAppContext.getString(titleRes));
         Log.e("bbb" ,context.getPackageName());
        mAlertDialog = new SpotsDialog(context , context.getString(titleRes));

        mAlertDialog.show();
    }

    /**
     * 隐藏正在加载的dialog
     */
    public static void dismissDialog(){
        mAlertDialog.dismiss();
    }
}
