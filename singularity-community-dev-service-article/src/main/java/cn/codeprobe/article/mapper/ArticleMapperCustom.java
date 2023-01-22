package cn.codeprobe.article.mapper;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.po.ArticleDO;

/**
 * 自定义文章发布
 *
 * @author Lionido
 */
public interface ArticleMapperCustom extends MyMapper<ArticleDO> {

    /**
     * 发布 预约发布的文章
     */
    void updateAppointToPublish();
}