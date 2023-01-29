package cn.codeprobe.article.mapper;

import org.springframework.stereotype.Repository;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.po.Comment;

/**
 * @author Lionido
 */
@Repository
public interface CommentMapper extends MyMapper<Comment> {}