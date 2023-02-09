package cn.codeprobe.article.schedule.consumer.listener;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import cn.codeprobe.api.config.RabbitMqDelay;
import cn.codeprobe.article.schedule.consumer.ConsumerService;
import cn.codeprobe.enums.RabbitMQ;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

/**
 * Delay RabbitMQ 文章消费端： 监听文章预约发布延迟消息
 * 
 * @author Lionido
 */
@Component
public class ArticlePublishListener {

    @Resource
    private ConsumerService consumerService;

    @RabbitListener(queues = {RabbitMqDelay.QUEUE_PUBLISH_DELAY})
    public void watch(String payload, Message message) {
        System.out.println("监听到延迟消息队列: " + new Date());
        System.out.println(payload);
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if (CharSequenceUtil.isNotBlank(routingKey)) {
            if (RabbitMQ.DELAY_PUBLISH.value.equalsIgnoreCase(routingKey)) {
                Map map = JSONUtil.toBean(payload, Map.class);
                String articleId = (String)map.get("articleId");
                // 消费端：接收到延迟预约发送消息，发布预约文章
                consumerService.publishAppointedArticle(articleId);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.RABBITMQ_ERROR);
        }
    }

}
