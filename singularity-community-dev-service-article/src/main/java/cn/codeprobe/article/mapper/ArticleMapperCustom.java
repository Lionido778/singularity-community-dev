package cn.codeprobe.article.mapper;

import org.springframework.stereotype.Repository;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.po.Article;

/**
 * 自定义文章发布
 *
 * @author Lionido
 */
@Repository
public interface ArticleMapperCustom extends MyMapper<Article> {

    /**
     * 发布 预约发布的文章
     */
    void updateAppointToPublish();
}