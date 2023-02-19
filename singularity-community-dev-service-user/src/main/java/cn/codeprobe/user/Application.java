package cn.codeprobe.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Lionido
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, RabbitAutoConfiguration.class})
@MapperScan("cn.codeprobe.user.mapper")
@ComponentScan({"cn.codeprobe"})
@EnableEurekaClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
