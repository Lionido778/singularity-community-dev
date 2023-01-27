package cn.codeprobe.pojo.vo;

/**
 * @author Lionido
 */
public class FansRegionRatioVO {

    private String name;
    private Long value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "FansRegionRatioVO{" + "name='" + name + '\'' + ", value=" + value + '}';
    }
}
