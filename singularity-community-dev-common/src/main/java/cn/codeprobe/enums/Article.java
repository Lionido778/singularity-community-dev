package cn.codeprobe.enums;

/**
 * @author Lionido
 */

public enum Article {

    /**
     * 文章状态
     */
    SELECT_ALL(0, "查询全部"),
    STATUS_VERIFYING(12, "审核中"),
    STATUS_MACHINE_VERIFYING(1, "机器审核中"),
    STATUS_MANUAL_VERIFYING(2, "人工审核中"),
    STATUS_APPROVED(3, "审核通过"),
    STATUS_REJECTED(4, "审核未通过"),
    STATUS_RECALLED(5, "文章撤回"),

    /**
     * 文章类型
     */
    HAS_COVER(1, "文章封面"),

    /**
     * 删除文章
     */
    DELETED(1, "删除"),
    UN_DELETED(0, "未删除"),

    /**
     * 发布文章
     */
    APPOINTED(1, "预约发布"),
    UN_APPOINTED(0, "及时发布"),

    /**
     * 人工审核
     */
    MANUAL_REVIEW_PASS(1, "文章人工审核通过！"),
    MANUAL_REVIEW_BLOCK(0, "文章人工审核失败！"),

    /**
     * 初始化设置
     */

    INITIAL_COMMENT_COUNTS(0, "评论数量"),
    INITIAL_READ_COUNTS(0, "阅读数量");


    public final Integer type;
    public final String desc;

    Article(Integer type, String dec) {
        this.type = type;
        this.desc = dec;
    }
}
