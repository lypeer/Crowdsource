package com.tesmple.crowdsource.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by lypeer on 10/17/2015.
 */
public class PushUtils {

    /**
     * 开启推送事物的方法
     *
     * @param handler        开启事物的对象里面的handler，用于传递异步消息
     * @param targetUsername 目标username，给这个username对应的机子发送推送
     * @param message        推送的信息内容
     */
    public static void startPushTransaction(final Handler handler, String targetUsername, String message) {
        AVQuery<AVUser> avQuery = new AVQuery<>("_User");
        avQuery.whereEqualTo("username" , targetUsername);
        avQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(e == null){

                }else {
                    Log.e("PushTrasanctionError" , e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.START_PUSH_TRANSACTION_FAILED;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 开启推送事物的方法
     *
     * @param handler        开启事物的对象里面的handler，用于传递异步消息
     * @param targetUsernames 目标username的string集合，给这些username对应的机子发送推送
     * @param message        推送的信息内容
     */
    public static void startPushTransaction(Handler handler, String[] targetUsernames, String message) {

    }
}
