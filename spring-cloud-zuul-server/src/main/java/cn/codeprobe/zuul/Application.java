package cn.codeprobe.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * zuul
 * 
 * @Description: @EnableZuulProxy zuul增强版 （zuul + eureka + ribbon）
 * @author Lionido
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MongoAutoConfiguration.class,
    RabbitAutoConfiguration.class, RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@EnableEurekaClient
@EnableZuulProxy
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
