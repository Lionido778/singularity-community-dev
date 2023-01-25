package cn.codeprobe.enums;

/**
 * 内容审核
 *
 * @author Lionido
 */

public enum ContentSecurity {

    /**
     * 文本内容安全
     */
    TEXT_SPAM("spam", "文字垃圾内容识别"), TEXT_POLITICS("politics", "文字敏感内容识别"), TEXT_ABUSE("abuse", "文字辱骂内容识别"),
    TEXT_TERRORISM("terrorism", "文字暴恐内容识别"), TEXT_PORN("porn", "文字鉴黄内容识别"), TEXT_FLOOD("flood", "文字灌水内容识别"),
    TEXT_CONTRABAND("contraband", "文字违禁内容识别"), TEXT_AD("ad", "文字广告内容识别"),

    /**
     * 审核结果
     */
    SUGGESTION_PASS("pass", "审核通过"), SUGGESTION_BLOCK("block", "审核失败"), SUGGESTION_REVIEW("review", "需要人工审核");

    public final String label;
    public final String desc;

    ContentSecurity(String label, String desc) {
        this.label = label;
        this.desc = desc;
    }
}
