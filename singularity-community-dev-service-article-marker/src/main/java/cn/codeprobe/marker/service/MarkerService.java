package cn.codeprobe.marker.service;

/**
 * @author Lionido
 */
public interface MarkerService {

    /**
     * MQ 发布静态页面
     *
     * @param articleId 文章ID
     * @param mongoId mongoId
     */
    void downloadHtml(String articleId, String mongoId);

    /**
     * MQ 删除已发布静态页面
     *
     * @param articleId 文章ID
     */
    void deleteHtml(String articleId);
}
