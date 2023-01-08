package cn.codeprobe.enums;

/**
 * 性别 枚举
 *
 * @author Lionido
 */
public enum UserSex {

    FEMALE(0, "女"),
    MALE(1, "男"),
    SECRET(2, "保密");

    public final Integer type;
    public final String value;

    UserSex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
