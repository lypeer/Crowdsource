package com.tesmple.crowdsource.object;

import java.io.Serializable;

/**
 *
 * Created by lypeer on 10/7/2015.
 */
public class Bill implements Serializable {

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
     * 申请者的手机号，使用字符串拼接的方式，用“=”拼接
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

    /**
     * 需要的人数
     */
    private String needNum;

    /**
     * 被接的时限
     */
    private String acceptDeadline;

    /**
     * 完成地点
     */
    private String location;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * 发布者的名字
     */
    private String publisherName;

    /**
     * 发布者的名字
     */
    private String publisherSchool;

    /**
     * 发布者头像的url
     */
    private String publisherHeadPortrait;

    public String getPublisherHeadPortrait() {
        return publisherHeadPortrait;
    }

    public void setPublisherHeadPortrait(String publisherHeadPortrait) {
        this.publisherHeadPortrait = publisherHeadPortrait;
    }

    public String getPublisherSchool() {
        return publisherSchool;
    }

    public void setPublisherSchool(String publisherSchool) {
        this.publisherSchool = publisherSchool;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAcceptDeadline() {
        return acceptDeadline;
    }

    public void setAcceptDeadline(String acceptDeadline) {
        this.acceptDeadline = acceptDeadline;
    }

    public String getNeedNum() {
        return needNum;
    }

    public void setNeedNum(String needNum) {
        this.needNum = needNum;
    }

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
