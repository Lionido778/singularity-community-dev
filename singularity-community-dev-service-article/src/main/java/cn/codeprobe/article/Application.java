package cn.codeprobe.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

/**
 *
 * @Description: @EnableFeignClients({"cn.codeprobe"}) 开启Feign
 * @author Lionido
 */
@SpringBootApplication
@MapperScan("cn.codeprobe.article.mapper")
@ComponentScan({"cn.codeprobe"})
@EnableEurekaClient
@EnableFeignClients({"cn.codeprobe.api.controller"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
