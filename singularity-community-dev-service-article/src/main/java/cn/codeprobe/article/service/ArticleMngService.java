package cn.codeprobe.article.service;

import java.util.Date;

import cn.codeprobe.result.page.PagedGridResult;

/**
 * 管理中心：文章服务
 *
 * @author Lionido
 */
public interface ArticleMngService {

    /**
     * 管理中心：查询所有文章列表
     *
     * @param keyword 关键词
     * @param status 状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param page 当前页
     * @param pageSize 当前页数量
     * @return 文章分页 PagedGridResult
     */
    PagedGridResult pageListAllArticles(Integer status, Integer page, Integer pageSize, String keyword, Date startDate,
        Date endDate);

    /**
     * 管理中心：人工审核
     *
     * @param articleId 文章ID
     * @param passOrNot 通过or拒绝
     */
    void manualReviewArticle(String articleId, Integer passOrNot);

}
