package cn.codeprobe.enums;

/**
 * @author Lionido
 */
public enum PageHelper {
    /**
     * 默认 分页
     */
    DEFAULT_PAGE(1, 10),

    /**
     * hot文章默认分页
     */
    ARTICLE_HOT_DEFAULT_PAGE(1, 5);

    public final Integer page;
    public final Integer pageSize;

    PageHelper(Integer page, Integer pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

}
