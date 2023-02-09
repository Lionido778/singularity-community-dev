package cn.codeprobe.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Delay RabbitMQ 延迟队列的配置
 * 
 * @author Lionido
 */
@Configuration
public class RabbitMqDelay {

    /** 定义交换机的名字 */
    public static final String EXCHANGE_DELAY = "exchange_delay";

    /** 定义序列的名字 */
    public static final String QUEUE_PUBLISH_DELAY = "queue_publish_delay";

    /** 创建交换机 delay ：开启延迟 */
    @Bean(EXCHANGE_DELAY)
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE_DELAY).durable(true).delayed().build();
    }

    /** 创建队列 */
    @Bean(QUEUE_PUBLISH_DELAY)
    public Queue queue() {
        return new Queue(QUEUE_PUBLISH_DELAY);
    }

    /** 队列绑定交换机 */
    @Bean
    public Binding delayBinding(@Qualifier(EXCHANGE_DELAY) Exchange exchange,
        @Qualifier(QUEUE_PUBLISH_DELAY) Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("delay.*.do").noargs();
    }

}
