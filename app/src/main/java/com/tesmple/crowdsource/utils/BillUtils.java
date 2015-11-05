package com.tesmple.crowdsource.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.R;
import com.tesmple.crowdsource.activity.App;
import com.tesmple.crowdsource.object.Bill;
import com.tesmple.crowdsource.object.Notification;
import com.tesmple.crowdsource.object.NotificationLab;
import com.tesmple.crowdsource.object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.helper.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Created by lypeer on 10/14/2015.
 */
public class BillUtils {

    /**
     * 装载AcceptableBillFragment的数据的list
     */
    private static List<Bill> acceptableBillList = new ArrayList<>();

    /**
     * 装载MyPublishFragment的数据的list
     */
    private static List<Bill> myPublishList = new ArrayList<>();

    /**
     * 装载AcceptedBillFragment的数据的list
     */
    private static List<Bill> acceptedBillList = new ArrayList<>();

    /**
     * 装载HistoryBillFragment的数据的list
     */
    private static List<Bill> historyBillList = new ArrayList<>();

    /**
     * 开启获得订单事物的方法
     *
     * @param targetFragment 开启事务的fragment的string值
     * @param handler        开启事务的fragment的handler
     *                       //     * @param isPublisher    用户是否为发布者，如果是就为true，不是则为false
     *                       //     * @param isApplicantor  用户是否为申请者，如果是就为true，不是则为false
     *                       //     * @param isComfirmer    用户是否为确认者，如果是就为true，不是则为false
     *                       //     * @param status         订单的状态
     * @param isLimit        是否需要分页
     * @param currentPage    如果需要分页的话，当前是第几页
     */
    public static void startGetBillTransaction(final String targetFragment,
                                               final Handler handler,
//                                               boolean isPublisher,
//                                               boolean isApplicantor,
//                                               boolean isComfirmer,
//                                               int status,
                                               boolean isLimit,
                                               int currentPage) {
        AVQuery<AVObject> avQuery = new AVQuery<>("Bill");
        switch (targetFragment) {
            case StringUtils.FRAGMENT_ACCEPTABLE_BILL:
                avQuery.whereNotEqualTo("publisher_phone", User.getInstance().getUserName());
                avQuery.orderByDescending("createdAt");
                avQuery.whereEqualTo("status", StringUtils.BILL_STATUS_ONE);

                break;
            case StringUtils.FRAGMENT_MY_PUBLISH:
                avQuery.whereEqualTo("publisher_phone", User.getInstance().getUserName());
                avQuery.orderByDescending("updatedAt");
                avQuery.whereNotEqualTo("status", StringUtils.BILL_STATUS_FIVE);
                break;
            case StringUtils.FRAGMENT_ACCEPTED_BILL:
                //表示我报名了还没有选定的筛选条件
                //此处存疑
                String[] statusAccptedBill = {StringUtils.BILL_STATUS_TWO, StringUtils.BILL_STATUS_THREE, StringUtils.BILL_STATUS_FOUR};
                AVQuery<AVObject> myApplicant = AVQuery.getQuery("Bill");
                myApplicant.whereContains("applicant", User.getInstance().getUserName());
                myApplicant.whereEqualTo("status", StringUtils.BILL_STATUS_ONE);
                //表示我被确认了并且订单的状态是进行中的筛选条件
                AVQuery<AVObject> myConfirm = AVQuery.getQuery("Bill");
                myConfirm.whereEqualTo("confirmer", User.getInstance().getUserName());
                myConfirm.whereContainedIn("status", Arrays.asList(statusAccptedBill));

                List<AVQuery<AVObject>> queries = new ArrayList<>();
                queries.add(myApplicant);
                queries.add(myConfirm);
                avQuery = AVQuery.or(queries);
                avQuery.orderByDescending("updatedAt");
                break;
            case StringUtils.FRAGMENT_HISTORY_BILL:
                String[] status = {StringUtils.BILL_STATUS_THREE, StringUtils.BILL_STATUS_FOUR};
                //表示我接的单完成或者是未完成的筛选条件
                AVQuery<AVObject> myAccept = AVQuery.getQuery("Bill");
                myAccept.whereEqualTo("confirmer", User.getInstance().getUserName());
                //表示我发的单完成或者是未完成的筛选条件
                AVQuery<AVObject> myPublish = AVQuery.getQuery("Bill");
                myPublish.whereEqualTo("publisher_phone", User.getInstance().getUserName());

                List<AVQuery<AVObject>> queries2 = new ArrayList<>();
                queries2.add(myAccept);
                queries2.add(myPublish);

                avQuery = AVQuery.or(queries2);
                avQuery.whereContainedIn("status", Arrays.asList(status));
                break;
        }
        // 采用缓存策略
        avQuery.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    for (AVObject avObject : list) {
                        if (avObject.getLong("deadline") < System.currentTimeMillis() && avObject.get("status").equals(StringUtils.BILL_STATUS_ONE)) {
                            avObject.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e != null) {
                                        Log.e("BillUtilsDeleteError", e.getMessage() + "===" + e.getCode());
                                    }
                                }
                            });
                        } else {
                            Bill bill = new Bill();
                            bill.setObjectId(avObject.getObjectId());
                            bill.setPublisherPhone((String) avObject.get("publisher_phone"));
                            bill.setAward((String) avObject.get("award"));
                            bill.setDetail((String) avObject.get("detail"));
                            bill.setDeadline(avObject.getLong("deadline"));
                            bill.setAddress((String) avObject.get("address"));
                            bill.setStatus((String) avObject.get("status"));
                            bill.setApplicant((String) avObject.get("applicant"));
                            bill.setConfirmer((String) avObject.get("confirmer"));
                            bill.setNeedNum((String) avObject.get("need_num"));
                            bill.setRobType((String) avObject.get("rob_type"));
                            bill.setLocation((String) avObject.get("location"));
                            bill.setAcceptDeadline((String) avObject.get("accept_deadline"));
                            bill.setContactWay((String) avObject.get("contact_way"));
                            switch (targetFragment) {
                                case StringUtils.FRAGMENT_ACCEPTABLE_BILL:
                                    if (userIsApplicant(bill)) {
                                        break;
                                    } else {
                                        acceptableBillList.add(bill);
                                    }
                                    break;
                                case StringUtils.FRAGMENT_MY_PUBLISH:
                                    myPublishList.add(bill);
                                    break;
                                case StringUtils.FRAGMENT_ACCEPTED_BILL:
                                    acceptedBillList.add(bill);
                                    break;
                                case StringUtils.FRAGMENT_HISTORY_BILL:
                                    historyBillList.add(bill);
                                    break;
                            }
                        }

                    }
                    Message message = new Message();
                    message.what = StringUtils.START_GET_BILL_TRANSACTION_SUCCESSFULLY;
                    handler.sendMessage(message);
                } else {
                    Log.e("PostRequestError", e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.START_GET_BILL_TRANSACTION_FAILED;
                    handler.sendMessage(message);
                }
            }
        });

        AVQuery<AVObject> avQuery1 = new AVQuery<>("UserHelper");
        avQuery1.whereEqualTo("username", User.getInstance().getUserName());
        avQuery1.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
        avQuery1.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    if (list.size() != 0) {
                        JSONArray jsonArray = list.get(0).getJSONArray("notification");
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Notification notification = new Notification();
                                try {
                                    JSONObject tempJsonObject = new JSONObject(jsonArray.get(i).toString());
                                    notification.setTime( tempJsonObject.get("time").toString());
                                    notification.setContent(tempJsonObject.getString("alert"));
                                    notification.setIsRead(tempJsonObject.getBoolean("is_read"));
                                    notification.setBillId(tempJsonObject.getString("bill_id"));
                                    notification.setPublisher(tempJsonObject.getString("sender"));
                                    notification.setType(PushUtils.getPushType(tempJsonObject.getString("alert")));
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                NotificationLab.getInstance().addNotification(notification);

                            }
                        }
                        NotificationLab.getInstance().reverseList();
                    }
                } else {
                    Log.e("BillUtilsUserHelper", e.getMessage() + "===" + e.getCode());
                }
            }
        });


    }

    /**
     * 获取订单的列表的方法
     *
     * @param targetFragment 申请获取订单的fragment的string值
     * @return 目标的订单列表
     */
    public static List<Bill> getBillsList(String targetFragment) {
        List<Bill> targetList = new ArrayList<>();
        if (targetFragment.equals(StringUtils.FRAGMENT_ACCEPTABLE_BILL)) {
            targetList = acceptableBillList;
        } else if (targetFragment.equals(StringUtils.FRAGMENT_MY_PUBLISH)) {
            targetList = myPublishList;
        } else if (targetFragment.equals(StringUtils.FRAGMENT_ACCEPTED_BILL)) {
            targetList = acceptedBillList;
        } else if (targetFragment.equals(StringUtils.FRAGMENT_HISTORY_BILL)) {
            targetList = historyBillList;
        }
        return targetList;
    }

    /**
     * 发布订单的方法
     *
     * @param handler 需要发布订单的那个界面的handler，如果发布成功的话就发送POST_REQUEST_SUCCESSFULLY，不成功的话发送POST_REQUEST_FAILED
     * @param bill    需要发布的bill
     */
    public static void publishBill(final Handler handler, Bill bill) {
        AVObject avObject = new AVObject("Bill");
        avObject.put("publisher_phone", bill.getPublisherPhone());
        avObject.put("publiser_school", bill.getPublisherSchool());
        avObject.put("award", bill.getAward());
        avObject.put("detail", bill.getDetail());
        avObject.put("deadline", bill.getDeadline());
        avObject.put("address", bill.getAddress());
        avObject.put("status", bill.getStatus());
        avObject.put("applicant", bill.getApplicant());
        avObject.put("confirmer", bill.getConfirmer());
        avObject.put("need_num", bill.getNeedNum());
        avObject.put("rob_type", bill.getRobType());
        avObject.put("location", bill.getLocation());
        avObject.put("accept_deadline", bill.getAcceptDeadline());
        avObject.put("contact_way", bill.getContactWay());
        avObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Message message = new Message();
                    message.what = StringUtils.POST_REQUEST_SUCCESSFULLY;
                    handler.sendMessage(message);
                } else {
                    Log.e("PostRequestError", e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.POST_REQUEST_FAILED;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 改变订单的状态
     *
     * @param handler      需要改变订单状态的fragment的handler
     * @param bill         需要改变订单状态的单
     * @param targetStatus 目标状态
     */
    public static void changeBillStatus(final Handler handler,
                                        final Bill bill,
                                        final String targetStatus) {
        AVQuery<AVObject> avObjectAVQuery = new AVQuery<>("Bill");
        avObjectAVQuery.getInBackground(bill.getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    avObject.put("status", targetStatus);
                    if (bill.getConfirmer() != null) {
                        avObject.put("confirmer", bill.getConfirmer());
                    }
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Message message = new Message();
                                message.what = StringUtils.CHANGE_BILL_STATUS_SUCCESSFULLY;
                                handler.sendMessage(message);
                            } else {
                                Log.e("ChangeAppliSaveError", e.getMessage() + "===" + e.getCode());
                                Message message = new Message();
                                message.what = StringUtils.CHANGE_BILL_STATUS_FAILED;
                                handler.sendMessage(message);
                            }
                        }
                    });
                } else {
                    Log.e("ChangeAppliGetError", e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.CHANGE_BILL_STATUS_FAILED;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 改变申请者的方法
     *
     * @param handler 申请改变申请者的fragment代表的string值
     * @param bill    需要改变applicantor的订单
     * @param isAdd   是否加入的布尔值，如果是加入就为true，如果不是就为false
     */
    public static void changeApplicantor(final Handler handler, final Bill bill, final boolean isAdd) {
        AVQuery<AVObject> avObjectAVQuery = new AVQuery<>("Bill");
        avObjectAVQuery.getInBackground(bill.getObjectId(), new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    if (isAdd) {
                        if (avObject.get("applicant") == null || avObject.get("applicant").equals("")) {
                            avObject.put("applicant", User.getInstance().getUserName());
                            Log.i("applicant", "要添加到空");
                        } else {
                            avObject.put("applicant", avObject.get("applicant") + "=" + User.getInstance().getUserName());
                            Log.i("applicant", "要添加进去");
                        }
                    } else {
                        //如果是要取消报名的话就将他的username从那里取消
                        if (bill.getApplicant().contains("=")) {
                            String[] applicantorArray = bill.getApplicant().split(App.getContext().getString(R.string.add));
                            StringBuilder newApplicant = new StringBuilder();
                            for (int i = 0; i < applicantorArray.length; i++) {
                                if (applicantorArray[i].equals(User.getInstance().getUserName())) {
                                    applicantorArray[i] = 0 + "";
                                    break;
                                }
                            }
                            for (int i = 0; i < applicantorArray.length; i++) {
                                if (!applicantorArray[i].equals("0")) {
                                    if (i == applicantorArray.length - 1) {
                                        newApplicant.append(applicantorArray[i]);
                                    } else {
                                        newApplicant.append(applicantorArray[i]).append("=");
                                    }
                                }
                            }
                            avObject.put("applicant", newApplicant.toString());
                        } else {
                            avObject.put("applicant", "");
                        }
                    }
                    avObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                Message message = new Message();
                                message.what = StringUtils.CHANGE_APPLICANT_SUCCESSFULLY;
                                handler.sendMessage(message);
                            } else {
                                Log.e("ChangeAppliSaveError", e.getMessage() + "===" + e.getCode());
                                Message message = new Message();
                                message.what = StringUtils.CHANGE_APPLICANT_FAILED;
                                handler.sendMessage(message);
                            }
                        }
                    });
                } else {
                    Log.e("ChangeAppliGetError", e.getMessage() + "===" + e.getCode());
                    Message message = new Message();
                    message.what = StringUtils.CHANGE_APPLICANT_FAILED;
                    handler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 清除装bill的list的方法
     *
     * @param targetFragment 指定清除list的fragment
     */
    public static void clearList(String targetFragment) {
        if (targetFragment.equals(StringUtils.FRAGMENT_ACCEPTABLE_BILL)) {
            acceptableBillList.clear();
        } else if (targetFragment.equals(StringUtils.FRAGMENT_MY_PUBLISH)) {
            myPublishList.clear();
        } else if (targetFragment.equals(StringUtils.FRAGMENT_ACCEPTED_BILL)) {
            acceptedBillList.clear();
        } else if (targetFragment.equals(StringUtils.FRAGMENT_HISTORY_BILL)) {
            historyBillList.clear();
        }
    }

    /**
     * 判断user是否已经报名了
     *
     * @param bill 传入目标bill
     * @return 返回布尔值，如果已经报名则true，否则false
     */
    public static boolean userIsApplicant(Bill bill) {
        if (bill.getApplicant() == null) {
            return false;
        } else {
            return bill.getApplicant().contains(User.getInstance().getUserName());
        }
        /*String[] applicantorArray = bill.getApplicant().split(App.getContext().getString(R.string.add));
        for (int i = 0; i < applicantorArray.length; i++) {
            if (applicantorArray[i].equals(User.getInstance().getUserName())) {
                return true;
            }
        }*/
    }
}