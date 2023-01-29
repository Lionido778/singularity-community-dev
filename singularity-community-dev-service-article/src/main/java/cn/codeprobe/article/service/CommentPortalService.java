package cn.codeprobe.article.service;

import cn.codeprobe.pojo.bo.NewCommentBO;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * @author Lionido
 */
public interface CommentPortalService {
    /**
     * 新增评论
     * 
     * @param newCommentBO 评论表单数据
     */
    void saveComment(NewCommentBO newCommentBO);

    /**
     * 获取文章总评论数
     * 
     * @param articleId 文章ID
     * @return 评论数
     */
    Integer countAllCommentsOfArticle(String articleId);

    /**
     * 获取文章评论列表
     * 
     * @param articleId 文章ID
     * @param page 当前页
     * @param pageSize 每页查询数量
     * @return PagedGridResult
     */
    PagedGridResult pageListComments(String articleId, Integer page, Integer pageSize);
}
