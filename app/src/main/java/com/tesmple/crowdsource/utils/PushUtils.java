package com.tesmple.crowdsource.utils;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVPush;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SendCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.User;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lypeer on 10/17/2015.
 */
public class PushUtils {

    /**
     * 标示异步过程中遍历了多少次的list
     */
    private static List<Boolean> sBooleanList1;

    /**
     * 标示异步过程中遍历了多少次的list
     */
    private static List<Boolean> sBooleanList2;

    /**
     * 获得的通知的size
     */
    private static int size;

    /**
     * 开启推送事物的方法
     *
     * @param handler  开启事物的对象里面的handler，用于传递异步消息
     * @param pushType 表示推送的方式
     * @param bill     具体的单
     */
    public static void startPushTransaction(final Handler handler, final String pushType, final Bill bill) {

        //首先对UserHelper进行读写操作
        final AVQuery<AVObject> avQuery = new AVQuery<>("UserHelper");
        switch (pushType) {
            //根据推送的类型的不同设置查找条件
            case StringUtils.PUSH_BECOME_APPLICANT:
            case StringUtils.PUSH_HAVE_ROBBED:
            case StringUtils.PUSH_CONFIRMER_REMOVE_BILL:
            case StringUtils.PUSH_REMIND_PUBLISHER:
            case StringUtils.PUSH_SYSTEM_FINISH:
                avQuery.whereEqualTo("username", bill.getPublisherPhone());
                break;
            case StringUtils.PUSH_BECOME_COMFIRMER:
            case StringUtils.PUSH_FINISH_BILL:
            case StringUtils.PUSH_PUBLISHER_REMOVE_BILL:
                avQuery.whereEqualTo("username", bill.getConfirmer());
                break;
            case StringUtils.PUSH_NOT_BECOME_COMFIRMER:
                String[] applicants = bill.getApplicant().split("=");
                String[] realApplicants = new String[applicants.length - 1];
                int i = 0;
                for (String s : applicants) {
                    if (!s.equals(bill.getConfirmer())) {
                        realApplicants[i] = s;
                        i++;
                    }
                }
                avQuery.whereEqualTo("username", Arrays.asList(realApplicants));
                break;
        }
        //开始寻找需要新建通知的单
        avQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e == null) {
                    //为了避免和后面得到的list混淆，此时采用一个final的对象来取代原本的list
                    final List<AVObject> userHelperList = list;
                    if (userHelperList.size() != 0) {
                        JSONObject jsonObject = new JSONObject();
                        String message = null;
                        switch (pushType) {
                            //根据推送类型的不同来设置推送的内容和发送者
                            case StringUtils.PUSH_BECOME_APPLICANT:
                                message = App.getContext().getString(R.string.push_become_applicant);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_BECOME_COMFIRMER:
                                message = App.getContext().getString(R.string.push_become_confirmer);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_NOT_BECOME_COMFIRMER:
                                message = App.getContext().getString(R.string.push_not_become_applicant);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_HAVE_ROBBED:
                                message = App.getContext().getString(R.string.push_have_robbed);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_PUBLISHER_REMOVE_BILL:
                                message = App.getContext().getString(R.string.push_publisher_remove_bill);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_CONFIRMER_REMOVE_BILL:
                                message = App.getContext().getString(R.string.push_confirmer_remove_bill);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_FINISH_BILL:
                                message = App.getContext().getString(R.string.push_finish_bill);
                                jsonObject.put("sender", User.getInstance().getUserName());
                                break;
                            case StringUtils.PUSH_REMIND_PUBLISHER:
                                message = App.getContext().getString(R.string.push_remind_publisher);
                                jsonObject.put("sender", "system");
                                break;
                            case StringUtils.PUSH_SYSTEM_FINISH:
                                message = App.getContext().getString(R.string.push_system_finish);
                                jsonObject.put("sender", "system");
                                break;
                        }
                        jsonObject.put("action", "com.tesmple.action");
                        jsonObject.put("alert", message);
                        jsonObject.put("is_read", false);
                        jsonObject.put("time", String.valueOf(System.currentTimeMillis()));
                        Log.e("PushUtils" , bill.getObjectId());
                        jsonObject.put("bill_id" , bill.getObjectId());
                        size = userHelperList.size();
                        sBooleanList1 = new ArrayList<>();
                        sBooleanList2 = new ArrayList<>();
                        for (int i = 0; i < userHelperList.size(); i++) {
                            JSONArray jsonArray = userHelperList.get(i).getJSONArray("notification");
                            //判断原先是否有通知，如果没有就新建一个，避免空异常
                            if (jsonArray != null) {
                                jsonArray.put(jsonObject);
                            } else {
                                jsonArray = new JSONArray();
                                jsonArray.put(jsonObject);
                            }
                            userHelperList.get(i).put("notification", jsonArray);
                            userHelperList.get(i).saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        sBooleanList1.add(true);
                                        AVQuery<AVObject> avQuery1 = new AVQuery<>("_User");
                                        avQuery1.whereEqualTo("username", userHelperList.get(sBooleanList1.size() - 1).get("username"));
                                        avQuery1.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
                                        avQuery1.findInBackground(new FindCallback<AVObject>() {
                                            @Override
                                            public void done(List<AVObject> list, AVException e) {
                                                if (e == null) {
                                                    if (list.size() != 0) {
                                                        final AVQuery pushQuery = AVInstallation.getQuery();
                                                        String message = null;
                                                        switch (pushType) {
                                                            case StringUtils.PUSH_BECOME_APPLICANT:
                                                                message = App.getContext().getString(R.string.push_become_applicant);
                                                                break;
                                                            case StringUtils.PUSH_BECOME_COMFIRMER:
                                                                message = App.getContext().getString(R.string.push_become_confirmer);
                                                                break;
                                                            case StringUtils.PUSH_NOT_BECOME_COMFIRMER:
                                                                message = App.getContext().getString(R.string.push_not_become_applicant);
                                                                break;
                                                            case StringUtils.PUSH_HAVE_ROBBED:
                                                                message = App.getContext().getString(R.string.push_have_robbed);
                                                                break;
                                                            case StringUtils.PUSH_PUBLISHER_REMOVE_BILL:
                                                                message = App.getContext().getString(R.string.push_publisher_remove_bill);
                                                                break;
                                                            case StringUtils.PUSH_CONFIRMER_REMOVE_BILL:
                                                                message = App.getContext().getString(R.string.push_confirmer_remove_bill);
                                                                break;
                                                            case StringUtils.PUSH_FINISH_BILL:
                                                                message = App.getContext().getString(R.string.push_finish_bill);
                                                                break;
                                                            case StringUtils.PUSH_REMIND_PUBLISHER:
                                                                message = App.getContext().getString(R.string.push_remind_publisher);
                                                                break;
                                                            case StringUtils.PUSH_SYSTEM_FINISH:
                                                                message = App.getContext().getString(R.string.push_system_finish);
                                                                break;
                                                        }
                                                        pushQuery.whereEqualTo("installationId", list.get(0).get("installationId"));
                                                        AVPush.sendMessageInBackground(message, pushQuery, new SendCallback() {
                                                            @Override
                                                            public void done(AVException e) {
                                                                if (e == null) {
                                                                    sBooleanList2.add(true);
                                                                    if (sBooleanList2.size() == size) {
                                                                        Message message1 = new Message();
                                                                        message1.what = StringUtils.PUSH_SUCCESSFULLY;
                                                                        handler.sendMessage(message1);
                                                                    }
                                                                } else {
                                                                    Log.e("PushUtilsSendError", e.getMessage() + "===" + e.getCode());
                                                                    Message message1 = new Message();
                                                                    message1.what = StringUtils.PUSH_FAILED;
                                                                    handler.sendMessage(message1);
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        Message message1 = new Message();
                                                        message1.what = StringUtils.PUSH_FAILED;
                                                        handler.sendMessage(message1);
                                                    }
                                                } else {
                                                    Log.e("PushUtilsFindError", e.getMessage() + "===" + e.getCode());
                                                    Message message1 = new Message();
                                                    message1.what = StringUtils.PUSH_FAILED;
                                                    handler.sendMessage(message1);
                                                }
                                            }
                                        });
                                    } else {
                                        Log.e("PushUtilsSaveError", e.getMessage() + "===" + e.getCode());
                                        Message message1 = new Message();
                                        message1.what = StringUtils.PUSH_FAILED;
                                        handler.sendMessage(message1);
                                    }
                                }
                            });
                        }
                    } else {
                        Message message1 = new Message();
                        message1.what = StringUtils.PUSH_FAILED;
                        handler.sendMessage(message1);
                    }
                } else {
                    Log.e("PushUtilsFind1Error", e.getMessage() + "===" + e.getCode());
                    Message message1 = new Message();
                    message1.what = StringUtils.PUSH_FAILED;
                    handler.sendMessage(message1);
                }
            }
        });
    }

    public static String getPushType(String content) {
        String type = null;
        if (content.equals(App.getContext().getString(R.string.push_become_applicant))) {
            type = StringUtils.PUSH_BECOME_APPLICANT;
        } else if (content.equals(App.getContext().getString(R.string.push_become_confirmer))) {
            type = StringUtils.PUSH_BECOME_COMFIRMER;
        } else if (content.equals(App.getContext().getString(R.string.push_not_become_applicant))) {
            type = StringUtils.PUSH_NOT_BECOME_COMFIRMER;
        } else if (content.equals(App.getContext().getString(R.string.push_have_robbed))) {
            type = StringUtils.PUSH_HAVE_ROBBED;
        } else if (content.equals(App.getContext().getString(R.string.push_publisher_remove_bill))) {
            type = StringUtils.PUSH_PUBLISHER_REMOVE_BILL;
        } else if (content.equals(App.getContext().getString(R.string.push_confirmer_remove_bill))) {
            type = StringUtils.PUSH_CONFIRMER_REMOVE_BILL;
        } else if (content.equals(App.getContext().getString(R.string.push_finish_bill))) {
            type = StringUtils.PUSH_FINISH_BILL;
        } else if (content.equals(App.getContext().getString(R.string.push_remind_publisher))) {
            type = StringUtils.PUSH_REMIND_PUBLISHER;
        } else if (content.equals(App.getContext().getString(R.string.push_system_finish))) {
            type = StringUtils.PUSH_SYSTEM_FINISH;
        }
        return type;
    }
}
