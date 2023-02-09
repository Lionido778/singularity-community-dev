package cn.codeprobe.article.schedule.produceor;

import java.util.Date;

/**
 * @author Lionido
 */
public interface ScheduleService {

    /**
     * 通过RabbitMQ 发送消息，让消费端下载HTML 优点：有利于并行，实现异步解耦
     *
     * @param articleId 文章ID
     * @param mongoId mongoId
     */
    void produceDownloadHtml(String articleId, String mongoId);

    /**
     * 通过RabbitMQ 发送消息，让消费端删除HTML 优点：有利于并行，实现异步解耦
     *
     * @param articleId 文章ID
     */
    void produceDeleteHtml(String articleId);

    /**
     * 创作中心：通过 MQ,发布预约发布文章 （延迟消息队列）
     *
     * @param publishTime 预约发布时间
     * @param articleId 文章ID
     */
    void producePublishAppointedArticle(String articleId, Date publishTime);
}
