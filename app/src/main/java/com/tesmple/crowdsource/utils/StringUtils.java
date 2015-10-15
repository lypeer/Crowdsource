package com.tesmple.crowdsource.utils;

/**
 *
 *  Created by lypeer on 10/7/2015.
 */
public class StringUtils {

    /**
     * 代表订单处于状态一，待报名状态的字符串字段
     */
    public static final String BILL_STATUS_ONE = "period_1";

    /**
     * 代表订单处于状态二，进行中状态的字符串字段
     */
    public static final String BILL_STATUS_TWO = "period_2";

    /**
     * 代表订单处于状态三，已完成状态的字符串字段
     */
    public static final String BILL_STATUS_THREE = "period_3";

    /**
     * 代表订单处于状态四，已删除状态的字符串字段
     */
    public static final String BILL_STATUS_FOUR = "period_4";

    /**
     * 代表被评论者是发单者
     */
    public static final String COMMENT_SENDER = "sender";

    /**
     * 代表被评论者是接单者
     */
    public static final String COMMENT_ACCEPTOR = "acceptor";

    /**
     * 手机号码输入框的最大长度
     */
    public static final int PHONE_NUMBER_MAX_LENGTH = 11;

    /**
     * 表示短信发送成功
     */
    public static final int SEND_SUCCESSFULLY = 21;

    /**
     * 表示使计时器减少1
     */
    public static final int COUNT_DOWN = 22;

    /**
     * 表示学号密码验证失败
     */
    public static final int VERIFY_STUNUM_FAILED = 23;

    /**
     * 表示学号密码验证成功
     */
    public static final int VERIFY_STUNUM_SUCCESSFULLY = 24;

    /**
     * 表示发布订单成功
     */
    public static final int POST_REQUEST_SUCCESSFULLY = 25;

    /**
     * 表示发布订单失败
     */
    public static final int POST_REQUEST_FAILED = 26;

    /**
     * 该String值代表AcceptableFragment
     */
    public static final String FRAGMENT_ACCEPTABLE_BILL = "fragment_acceptable_bill";

    /**
     * 该String值代表AcceptedFragment
     */
    public static final String FRAGMENT_ACCEPTED_BILL = "fragment_accepted_bill";

    /**
     * 该String值代表MyPublishFragment
     */
    public static final String FRAGMENT_MY_PUBLISH = "fragment_my_publish";

    /**
     * 该String值代表HistoryBillFragment
     */
    public static final String FRAGMENT_HISTORY_BILL = "fragment_history_bill";

    /**
     * 电子科技大学的string值
     */
    public static final String UESTC = "电子科技大学";
}
