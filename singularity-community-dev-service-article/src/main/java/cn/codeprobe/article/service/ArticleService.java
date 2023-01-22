package cn.codeprobe.article.service;

import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.result.page.PagedGridResult;

import java.util.Date;

/**
 * 文章服务
 *
 * @author Lionido
 */
public interface ArticleService {


    /**
     * 新增文章
     *
     * @param newArticleBO 文章BO
     */
    void saveArticle(NewArticleBO newArticleBO);

    /**
     * 查询文章列表
     *
     * @param userId    用户ID
     * @param keyword   关键词
     * @param status    文章状态
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @param page      当前页
     * @param pageSize  每页数量
     * @return 文章分页列表
     */
    PagedGridResult pageListUsers(String userId, String keyword, Integer status,
                                  Date startDate, Date endDate, Integer page, Integer pageSize);

    /**
     * 通过文章ID 逻辑删除
     *
     * @param userId    文章作者
     * @param articleId 文章ID
     */
    void removeArticleByArticleId(String userId, String articleId);

    /**
     * 发布预约发布文章
     */
    void publishAppointedArticle();
}
