package cn.codeprobe.enums;

/**
 * 友情链接枚举
 * 
 * @author Lionido
 */

public enum FriendLink {

    /**
     * 删除文章
     */
    DELETED(1, "删除"), UN_DELETED(0, "未删除");

    public final Integer type;
    public final String desc;

    FriendLink(Integer type, String dec) {
        this.type = type;
        this.desc = dec;
    }
}
