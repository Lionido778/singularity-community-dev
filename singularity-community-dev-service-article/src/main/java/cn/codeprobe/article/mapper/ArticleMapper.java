package cn.codeprobe.article.mapper;

import org.springframework.stereotype.Repository;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.po.Article;

/**
 * @author Lionido
 */
@Repository
public interface ArticleMapper extends MyMapper<Article> {}