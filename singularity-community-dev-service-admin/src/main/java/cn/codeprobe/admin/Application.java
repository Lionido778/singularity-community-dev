package cn.codeprobe.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Lionido
 */
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@MapperScan("cn.codeprobe.admin.mapper")
@ComponentScan({"cn.codeprobe"})
@EnableEurekaClient
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
