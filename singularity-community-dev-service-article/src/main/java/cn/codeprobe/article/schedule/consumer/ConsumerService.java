package cn.codeprobe.article.schedule.consumer;

/**
 * 文章消费端
 * 
 * @author Lionido
 */
public interface ConsumerService {

    /**
     * Delay RabbitMQ消费端：发布预约文章
     * 
     * @param articleId 文章ID
     */
    void publishAppointedArticle(String articleId);
}
