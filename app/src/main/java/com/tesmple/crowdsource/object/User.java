package com.tesmple.crowdsource.object;

/**
 *
 * Created by lypeer on 10/7/2015.
 */
public class User {

    /**
     * 用户的对象，这里用的是懒汉模式，等到需要的时候在创建对象
     */
    private static User sUser = null;

    /**
     * 用户的构造方法，将其私有化使得无法创建对象
     */
    private User(){

    }

    /**
     * 获得user的实例的方法
     * @return  user的实例
     */
    public static User getInstance(){
        if(sUser == null){
            sUser = new User();
        }
        return sUser;
    }

    /**
     * 用户的手机号
     */
    private String userName;

    /**
     * 用户登录的时候的密码
     */
    private String password;

    /**
     * 用户的学号
     */
    private String stuNum;

    /**
     * 用户的学校
     */
    private String school;

    /**
     * 用户的姓名
     */
    private String name;

    /**
     * 用户的性别
     */
    private String gender;

    /**
     * 用户头像的uri值
     */
    private String headProtrait;

    /**
     * 用户的信用星级
     */
    private String creditValue;

    /**
     * 发单的信用，使用字符串拼接的方式，用“+”拼接
     */
    private String sendStar;

    /**
     * 接单的信用，使用字符串拼接的方式，用“+”拼接
     */
    private String acceptStar;

    /**
     * 用户当前的状态
     */
    private String status;

    /**
     * 用户的昵称
     */
    private String nickName;

    /**
     * 用户的学院
     */
    private String department;

    /**
     * 用户的专业
     */
    private String major;

    /**
     * 用户的年级
     */
    private String grade;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAcceptStar() {
        return acceptStar;
    }

    public void setAcceptStar(String acceptStar) {
        this.acceptStar = acceptStar;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeadProtrait() {
        return headProtrait;
    }

    public void setHeadProtrait(String headProtrait) {
        this.headProtrait = headProtrait;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSendStar() {
        return sendStar;
    }

    public void setSendStar(String sendStar) {
        this.sendStar = sendStar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStuNum() {
        return stuNum;
    }

    public void setStuNum(String stuNum) {
        this.stuNum = stuNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
