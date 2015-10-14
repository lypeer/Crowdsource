package com.tesmple.crowdsource.utils;

import android.os.Handler;

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
     * @param targetFragment 开启事务的fragment的string值
     * @param handler 开启事务的fragment的handler
     * @param isPublisher 用户是否为发布者，如果是就为true，不是则为false
     * @param isApplicantor 用户是否为申请者，如果是就为true，不是则为false
     * @param isComfirmer 用户是否为确认者，如果是就为true，不是则为false
     * @param status 订单的状态
     * @param isLimit 是否需要分页
     * @param currentPage 如果需要分页的话，当前是第几页
     */
    public static void startGetBillTransaction(String targetFragment ,
                                               Handler handler ,
                                               boolean isPublisher ,
                                               boolean isApplicantor ,
                                               boolean isComfirmer ,
                                               int status ,
                                               boolean isLimit ,
                                               int currentPage){

    }
}
