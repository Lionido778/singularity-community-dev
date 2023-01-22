package cn.codeprobe.article.task;

import cn.codeprobe.article.service.ArticleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 文章定时发布任务
 *
 * @author Lionido
 */
@Configuration
@EnableScheduling
public class ArticleAppointPublishTask {

    @Resource
    private ArticleService articleService;

    @Scheduled(cron = "0/3 * * * * ? ")
    public void publishArticleAppointed() {
        System.out.println(LocalDate.now());
        articleService.publishAppointedArticle();
    }


}
