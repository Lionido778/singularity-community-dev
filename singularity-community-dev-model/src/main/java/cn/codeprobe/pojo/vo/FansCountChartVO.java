package cn.codeprobe.pojo.vo;

/**
 * @author Lionido
 */
public class FansCountChartVO {

    private Long manCounts;
    private Long womanCounts;

    public Long getManCounts() {
        return manCounts;
    }

    public void setManCounts(Long manCounts) {
        this.manCounts = manCounts;
    }

    public Long getWomanCounts() {
        return womanCounts;
    }

    public void setWomanCounts(Long womanCounts) {
        this.womanCounts = womanCounts;
    }

    @Override
    public String toString() {
        return "FansCountEchartVO{" + "manCounts=" + manCounts + ", womanCounts=" + womanCounts + '}';
    }
}
