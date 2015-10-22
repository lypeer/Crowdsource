package com.tesmple.crowdsource.utils;

/**
 *
 *  Created by lypeer on 10/7/2015.
 */
public class StringUtils {

    /**
     * 代表订单处于状态一，待报名状态的字符串字段
     */
    public static final String BILL_STATUS_ONE = "待报名";

    /**
     * 代表订单处于状态二，进行中状态的字符串字段
     */
    public static final String BILL_STATUS_TWO = "进行中";

    /**
     * 代表订单处于状态三，已完成状态的字符串字段
     */
    public static final String BILL_STATUS_THREE = "已完成";

    /**
     * 代表订单处于状态四，未完成状态的字符串字段
     */
    public static final String BILL_STATUS_FOUR = "未完成";

    /**
     * 代表订单处于状态五，已删除状态的字符串字段
     */
    public static final String BILL_STATUS_FIVE = "已删除";

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
     * 表示更改报名者成功
     */
    public static final int CHANGE_APPLICANT_SUCCESSFULLY = 25;

    /**
     * 表示更改报名者失败
     */
    public static final int CHANGE_APPLICANT_FAILED = 26;

    /**
     * 表示发布订单成功
     */
    public static final int POST_REQUEST_SUCCESSFULLY = 27;

    /**
     * 表示发布订单失败
     */
    public static final int POST_REQUEST_FAILED = 28;

    /**
     * 表示更改订单状态成功
     */
    public static final int CHANGE_BILL_STATUS_SUCCESSFULLY = 29;

    /**
     * 表示更改订单状态失败
     */
    public static final int CHANGE_BILL_STATUS_FAILED = 30;

    /**
     * 表示开启获得订单的事务成功
     */
    public static final int START_GET_BILL_TRANSACTION_SUCCESSFULLY = 31;

    /**
     * 表示开启获得订单的事务失败
     */
    public static final int START_GET_BILL_TRANSACTION_FAILED = 32;

    /**
     * 表示开启推送的事务成功
     */
    public static final int START_PUSH_TRANSACTION_SUCCESSFULLY = 33;

    /**
     * 表示开启推送的事务失败
     */
    public static final int START_PUSH_TRANSACTION_FAILED = 34;

    /**
     * 表示获取billcomment成功
     */
    public static final int START_GET_BILL_COMMENT_SUCCESSFULLY = 35;

    /**
     * 表示获取billcomment失败
     */
    public static final int START_GET_BILL_COMMENT_FAILED = 36;

    /**
     * 表示由于网络原因导致登录失败
     */
    public static final int NETWORK_ERROE = 37;

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
     * 该String值代表CommentFragment
     */
    public static final String FRAGMENT_BILL_COMMENT = "fragment_bill_comment";

    /**
     * 电子科技大学的string值
     */
    public static final String UESTC = "电子科技大学";
}
