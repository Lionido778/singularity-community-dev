package cn.codeprobe.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Lionido
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.codeprobe.user.mapper")
@ComponentScan(basePackages = {"cn.codeprobe"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
