package cn.codeprobe.article.controller;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.article.ArticleWriterControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.ArticleWriterService;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * 用户文章服务接口
 *
 * @author Lionido
 */
@RestController
public class ArticleWriterController extends ApiController implements ArticleWriterControllerApi {

    @Resource
    private ArticleWriterService articleWriterService;

    @Override
    public JsonResult addNewArticle(NewArticleBO newArticleBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }
        // 调用 service 执行发文
        articleWriterService.saveArticle(newArticleBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult withdrawArticle(String userId, String articleId) {
        // 数据校验
        if (CharSequenceUtil.isBlank(articleId) || CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
        // 调用service 执行撤回操作
        articleWriterService.withdrawArticle(articleId, userId);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryPageListArticles(String userId, String keyword, Integer status, Date startDate, Date endDate,
        Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        if (CharSequenceUtil.isBlank(userId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        // 校验status
        if (status != null && !status.equals(Article.STATUS_VERIFYING.type) && !status.equals(Article.SELECT_ALL.type)
            && !status.equals(Article.STATUS_APPROVED.type) && !status.equals(Article.STATUS_REJECTED.type)
            && !status.equals(Article.STATUS_RECALLED.type)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult =
            articleWriterService.pageListArticles(userId, keyword, status, startDate, endDate, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult deleteArticle(String userId, String articleId) {
        if (CharSequenceUtil.isNotBlank(userId)) {
            if (CharSequenceUtil.isNotBlank(articleId)) {
                articleWriterService.removeArticleByArticleId(userId, articleId);
            } else {
                return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        return JsonResult.ok();
    }
}
