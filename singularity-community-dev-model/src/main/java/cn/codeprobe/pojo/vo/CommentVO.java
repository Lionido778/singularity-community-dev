package cn.codeprobe.pojo.vo;

import java.util.Date;

/**
 * @author Lionido
 */
public class CommentVO {

    /** 评论ID */
    private String commentId;
    /** 父评论 */
    private String fatherId;
    /** 文章作者ID */
    private String articleId;
    /** 评论者ID */
    private String commentUserId;
    /** 评论者昵称 */
    private String commentUserNickname;
    /** 评论者头像 */
    private String commentUserFace;
    /** 评论内容 */
    private String content;
    /** 评论发表时间 */
    private Date createTime;
    /** 关联表字段 父评论昵称 */
    private String quoteUserNickname;
    /** 关联表字段 父评论内容 */
    private String quoteContent;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserNickname() {
        return commentUserNickname;
    }

    public void setCommentUserNickname(String commentUserNickname) {
        this.commentUserNickname = commentUserNickname;
    }

    public String getCommentUserFace() {
        return commentUserFace;
    }

    public void setCommentUserFace(String commentUserFace) {
        this.commentUserFace = commentUserFace;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getQuoteUserNickname() {
        return quoteUserNickname;
    }

    public void setQuoteUserNickname(String quoteUserNickname) {
        this.quoteUserNickname = quoteUserNickname;
    }

    public String getQuoteContent() {
        return quoteContent;
    }

    public void setQuoteContent(String quoteContent) {
        this.quoteContent = quoteContent;
    }

    @Override
    public String toString() {
        return "CommentVO{" +
                "commentId='" + commentId + '\'' +
                ", fatherId='" + fatherId + '\'' +
                ", articleId='" + articleId + '\'' +
                ", commentUserId='" + commentUserId + '\'' +
                ", commentUserNickname='" + commentUserNickname + '\'' +
                ", commentUserFace='" + commentUserFace + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", quoteUserNickname='" + quoteUserNickname + '\'' +
                ", quoteContent='" + quoteContent + '\'' +
                '}';
    }
}