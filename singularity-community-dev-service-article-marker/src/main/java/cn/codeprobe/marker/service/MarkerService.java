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
    public String publishHtml(String articleId, String mongoId);

}
