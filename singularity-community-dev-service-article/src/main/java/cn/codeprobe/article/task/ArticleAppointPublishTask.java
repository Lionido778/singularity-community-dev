package cn.codeprobe.article.task;

import java.time.LocalDate;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;

import cn.codeprobe.article.service.ArticleWriterService;

/**
 * 文章定时发布任务
 *
 * @author Lionido @ Configuration 标记配置类 @ EnableScheduling 开启定时任务
 */
public class ArticleAppointPublishTask {

    @Resource
    private ArticleWriterService articleWriterService;

    @Scheduled(cron = "0/3 * * * * ? ")
    public void publishArticleAppointed() {
        System.out.println(LocalDate.now());
        articleWriterService.publishAppointedArticle();
    }

}
