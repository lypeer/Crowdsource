package com.tesmple.crowdsource.object;

import java.util.Date;

/**
 * Created by lypeer on 10/7/2015.
 */
public class UserComment {

    /**
     * 发布评论的人的手机号码
     */
    private String publisher;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * user此时的身份，有两种，sender和acceptor
     */
    private String type;

    /**
     * 评论者打的星级
     */
    private String star;

    /**
     * 评论生成的时间
     */
    private Date createAt;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
