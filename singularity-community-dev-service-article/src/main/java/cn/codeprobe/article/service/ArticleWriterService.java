package cn.codeprobe.article.service;

import java.util.Date;

import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * 创作中心：文章服务
 *
 * @author Lionido
 */
public interface ArticleWriterService {

    /**
     * 创作中心：新增文章
     *
     * @param newArticleBO 文章BO
     */
    void saveArticle(NewArticleBO newArticleBO);

    /**
     * 创作中心：查询文章列表
     *
     * @param userId 用户ID
     * @param keyword 关键词
     * @param status 文章状态
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param page 当前页
     * @param pageSize 每页数量
     * @return 文章分页列表
     */
    PagedGridResult pageListArticles(String userId, String keyword, Integer status, Date startDate, Date endDate,
        Integer page, Integer pageSize);

    /**
     * 创作中心：通过文章ID 逻辑删除
     *
     * @param userId 文章作者
     * @param articleId 文章ID
     */
    void removeArticleByArticleId(String userId, String articleId);

    /**
     * 创作中心：发布预约发布文章
     */
    void publishAppointedArticle();

    /**
     * 创作中心：撤回文章
     *
     * @param articleId 文章ID
     * @param userId 用户ID
     */
    void withdrawArticle(String articleId, String userId);
}
