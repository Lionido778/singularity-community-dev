package cn.codeprobe.article.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.po.Comment;
import cn.codeprobe.pojo.vo.CommentVO;

/**
 * @author Lionido
 */
@Repository
public interface CommentMapperCustom extends MyMapper<Comment> {
    /**
     * 通过文章Id获取所有评论
     * 
     * @param articleId 文章Id
     * @return List<CommentVO>
     */
    List<CommentVO> selectAllByArticleId(String articleId);
}