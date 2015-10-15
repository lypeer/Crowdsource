package com.tesmple.crowdsource.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.tesmple.crowdsource.object.Bill;

import java.util.ArrayList;
import java.util.List;

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
     * @param isPublisher    用户是否为发布者，如果是就为true，不是则为false
     * @param isApplicantor  用户是否为申请者，如果是就为true，不是则为false
     * @param isComfirmer    用户是否为确认者，如果是就为true，不是则为false
     * @param status         订单的状态
     * @param isLimit        是否需要分页
     * @param currentPage    如果需要分页的话，当前是第几页
     */
    public static void startGetBillTransaction(String targetFragment,
                                               Handler handler,
                                               boolean isPublisher,
                                               boolean isApplicantor,
                                               boolean isComfirmer,
                                               int status,
                                               boolean isLimit,
                                               int currentPage) {

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
     * @param handler
     * @param bill
     * @param targetStatus
     * @param isPublisher
     */
    public static void changeBillStatus(Handler handler,
                                        Bill bill,
                                        String targetStatus,
                                        boolean isPublisher) {

    }
}
