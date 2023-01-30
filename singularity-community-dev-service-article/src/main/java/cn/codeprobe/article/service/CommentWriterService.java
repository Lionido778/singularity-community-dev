package cn.codeprobe.article.service;

import cn.codeprobe.result.page.PagedGridResult;

/**
 * @author Lionido
 */
public interface CommentWriterService {
    /**
     * 通过作家ID查询所有相关评论
     * 
     * @param writerId 作家ID
     * @param page 当前夜
     * @param pageSize 每页查询数量
     * @return PagedGridResult
     */
    PagedGridResult pageListCommentByWriterId(String writerId, Integer page, Integer pageSize);

    /**
     * 删除文章评论
     * 
     * @param writerId 作家Id
     * @param commentId 评论ID
     */
    void removeComment(String writerId, String commentId);
}
