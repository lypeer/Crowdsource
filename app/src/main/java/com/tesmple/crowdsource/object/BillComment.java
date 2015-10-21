package com.tesmple.crowdsource.object;

import android.util.Log;

import java.util.Date;

/**
 * Created by lypeer on 10/7/2015.
 */
public class BillComment {

    /**
     * 评论依附的单的objectId
     */
    private String whichBill;

    /**
     * 发布订单者的手机号码
     */
    private String publisher;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 评论生成的时候的date值
     */
    private Long creatAt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreatAt() {
        return creatAt;
    }

    public void setCreatAt(Long creatAt) {
        this.creatAt = creatAt;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getWhichBill() {
        return whichBill;
    }

    public void setWhichBill(String whichBill) {
        this.whichBill = whichBill;
    }
}
