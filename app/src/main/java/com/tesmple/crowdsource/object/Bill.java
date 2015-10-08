package com.tesmple.crowdsource.object;

/**
 *
 * Created by lypeer on 10/7/2015.
 */
public class Bill {

    /**
     * 发布者的手机号
     */
    private String publisherPhone;

    /**
     * 订单的奖励
     */
    private String award;

    /**
     * 订单详情
     */
    private String detail;

    /**
     * 订单完成的最后期限
     */
    private Long deadline;

    /**
     * 任务完成的大致地址
     */
    private String address;

    /**
     * 订单当前的状态
     */
    private String status;

    /**
     * 申请者的手机号，使用字符串拼接的方式，用“+”拼接
     */
    private String applicant;

    /**
     * 确认接单的人的手机号
     */
    private String confirmer;

    /**
     * 订单的objectId
     */
    private String objectId;

    /**
     * 订单的状态，四种订单状态，状态一为待报名，状态二为进行中，状态三为已完成，状态四为已删除
     */
    private String robType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getConfirmer() {
        return confirmer;
    }

    public void setConfirmer(String confirmer) {
        this.confirmer = confirmer;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPublisherPhone() {
        return publisherPhone;
    }

    public void setPublisherPhone(String publisherPhone) {
        this.publisherPhone = publisherPhone;
    }

    public String getRobType() {
        return robType;
    }

    public void setRobType(String robType) {
        this.robType = robType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
