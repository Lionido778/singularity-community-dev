package cn.codeprobe.article.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.codeprobe.article.base.CommentBaseService;
import cn.codeprobe.article.service.CommentWriterService;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Comment;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * @author Lionido
 */
@Service
public class CommentWriterServiceImpl extends CommentBaseService implements CommentWriterService {
    @Override
    public PagedGridResult pageListCommentByWriterId(String writerId, Integer page, Integer pageSize) {
        Comment comment = new Comment();
        comment.setWriterId(writerId);
        List<Comment> commentList = commentMapper.select(comment);
        return setterPageGrid(commentList, page);
    }

    @Override
    public void removeComment(String writerId, String commentId) {
        Comment comment = new Comment();
        comment.setWriterId(writerId);
        comment.setId(commentId);
        int result = commentMapper.delete(comment);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_COMMENT_DELETE_FAILED);
        }
    }
}
