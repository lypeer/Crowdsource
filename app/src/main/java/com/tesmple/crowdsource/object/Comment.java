package com.tesmple.crowdsource.object;

/**
 * Created by ESIR on 2015/10/18.
 */
public class Comment {
    /**
     * 评论者头像的url
     */
    private String commentHeadPortrait;

    /**
     * 评论者的名字
     */
    private String commentName;

    /**
     * 评论者学校
     */
    private String commentSchool;

    /**
     * 评论详情
     */
    private String commentDetail;

    /**
     * 评论是否点赞
     */
    private boolean commentFavorite;

    /**
     * 评论点赞数
     */
    private int commnetFavoriteNum;

    public String getCommentHeadPortrait() {
        return commentHeadPortrait;
    }

    public String getCommentName() {
        return commentName;
    }

    public String getCommentSchool() {
        return commentSchool;
    }

    public String getCommentDetail() {
        return commentDetail;
    }

    public boolean isCommentFavorite() {
        return commentFavorite;
    }

    public int getCommnetFavoriteNum() {
        return commnetFavoriteNum;
    }

    public void setCommentHeadPortrait(String commentHeadPortrait) {
        this.commentHeadPortrait = commentHeadPortrait;
    }

    public void setCommentName(String commentName) {
        this.commentName = commentName;
    }

    public void setCommentSchool(String commentSchool) {
        this.commentSchool = commentSchool;
    }

    public void setCommentDetail(String commentDetail) {
        this.commentDetail = commentDetail;
    }

    public void setCommentFavorite(boolean commentFavorite) {
        this.commentFavorite = commentFavorite;
    }

    public void setCommnetFavoriteNum(int commnetFavoriteNum) {
        this.commnetFavoriteNum = commnetFavoriteNum;
    }
}
