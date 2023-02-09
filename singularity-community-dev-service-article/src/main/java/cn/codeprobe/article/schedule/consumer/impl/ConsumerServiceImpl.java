package cn.codeprobe.article.schedule.consumer.impl;

import org.springframework.stereotype.Service;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.schedule.consumer.ConsumerService;

/**
 * @author Lionido
 */
@Service
public class ConsumerServiceImpl extends ArticleBaseService implements ConsumerService {

    @Override
    public void publishAppointedArticle(String articleId) {
        publishArticle(articleId);
    }
}
