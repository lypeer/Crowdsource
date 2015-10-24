package com.tesmple.crowdsource.object;

/**
 * Created by ESIR on 2015/10/18.
 */
public class Applicant {

    /**
     * 报名者的username
     */
    private String username;

    /**
     * 报名者头像的url
     */
    private String applicantHeadPortrait;

    /**
     * 报名者的名字
     */
    private String applicantName;

    /**
     * 报名者学校
     */
    private String applicantSchool;

    /**
     * 报名者是否被选中
     */
    private Boolean applicantIsChecked;

    /**
     * 报名者信用度
     */
    private int applicantCreditValue;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getApplicantHeadPortrait() {
        return applicantHeadPortrait;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantSchool() {
        return applicantSchool;
    }

    public Boolean getApplicantIsChecked() {
        return applicantIsChecked;
    }

    public int getApplicantCreditValue() {
        return applicantCreditValue;
    }

    public void setApplicantHeadPortrait(String applicantHeadPortrait) {
        this.applicantHeadPortrait = applicantHeadPortrait;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public void setApplicantSchool(String applicantSchool) {
        this.applicantSchool = applicantSchool;
    }

    public void setApplicantIsChecked(Boolean applicantIsChecked) {
        this.applicantIsChecked = applicantIsChecked;
    }

    public void setApplicantCreditValue(int applicantCreditValue) {
        this.applicantCreditValue = applicantCreditValue;
    }
}
