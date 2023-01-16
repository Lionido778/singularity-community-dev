package cn.codeprobe.enums;

/**
 * @author Lionido
 */

public enum PageHelper {

    DEFAULT_PAGE(1, 10);

    public final Integer page;
    public final Integer pageSize;

    PageHelper(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

}
