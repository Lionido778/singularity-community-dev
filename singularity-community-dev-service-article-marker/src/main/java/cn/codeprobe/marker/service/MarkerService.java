package cn.codeprobe.marker.service;

/**
 * @author Lionido
 */
public interface MarkerService {

    /**
     * 发布静态页面
     * 
     * @param articleId 文章ID
     * @param mongoId mongoId
     * @return ok
     */
    String publishHtml(String articleId, String mongoId);

    /**
     * MQ 发布静态页面
     *
     * @param articleId 文章ID
     * @param mongoId mongoId
     * @return ok
     */
    void publishHtmlByMq(String articleId, String mongoId);

    /**
     * 删除已发布静态页面
     *
     * @param articleId 文章ID
     * @return ok
     */
    String deleteHtml(String articleId);

    /**
     * MQ 删除已发布静态页面
     *
     * @param articleId 文章ID
     * @return ok
     */
    void deleteHtmlByMq(String articleId);
}
