package cn.codeprobe.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Lionido
 */
@SpringBootApplication
@MapperScan("cn.codeprobe.article.mapper")
@ComponentScan({"cn.codeprobe"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}