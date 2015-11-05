package com.tesmple.crowdsource.object;

/**
 * Created by lypeer on 10/23/2015.
 */
public class Notification {

    /**
     * 表示此通知的属性类型
     */
    private String type;

    /**
     * 如果为人的话就为他的username，如果是系统通知的话就为system
     */
    private String publisher;

    /**
     * 通知发布的时间的毫秒值
     */
    private String time;

    /**
     * 通知的内容
     */
    private String content;

    /**
     * bill的objectid
     */
    private String billId;

    /**
     * 用户是否已读这条通知，如果已读就为true，如果未读就为false
     */
    private boolean isRead;

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
