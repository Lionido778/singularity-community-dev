package cn.codeprobe.enums;

/**
 * @author Lionido
 */

public enum MybatisResult {
    /**
     * 持久层 操作是否成功呢
     */
    SUCCESS(1, "持久层操作成功！");


    public final Integer result;
    public final String desc;

    MybatisResult(Integer result, String desc) {
        this.result = result;
        this.desc = desc;
    }


}
