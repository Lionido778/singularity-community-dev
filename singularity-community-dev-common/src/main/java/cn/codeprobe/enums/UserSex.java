package cn.codeprobe.enums;

/**
 * @Desc: 性别 枚举
 */
public enum UserSex {
    female(0, "女"),
    male(1, "男"),
    secret(2, "保密");

    public final Integer type;
    public final String value;

    UserSex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
