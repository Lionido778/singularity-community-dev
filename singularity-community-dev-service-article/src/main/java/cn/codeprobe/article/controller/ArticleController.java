package cn.codeprobe.article.controller;

import cn.codeprobe.api.controller.article.ArticleControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.ArticleService;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 用户文章服务接口
 *
 * @author Lionido
 */
@RestController
public class ArticleController extends ApiController implements ArticleControllerApi {

    @Resource
    private ArticleService articleService;


    @Override
    public JsonResult addNewArticle(NewArticleBO newArticleBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }
        // 调用 service 执行发文
        articleService.saveArticle(newArticleBO);
        return JsonResult.ok();
    }


    @Override
    public JsonResult reviewArticle(String articleId, Integer passOrNot) {
        // 数据校验
        if (CharSequenceUtil.isBlank(articleId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        if (passOrNot == null) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        } else {
            if (!passOrNot.equals(Article.MANUAL_REVIEW_PASS.type)
                    && !passOrNot.equals(Article.MANUAL_REVIEW_BLOCK.type)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
            }
        }
        // 调用service 进行人工审核
        articleService.manualReviewArticle(articleId, passOrNot);
        return JsonResult.ok();
    }

    @Override
    public JsonResult withdrawArticle(String userId, String articleId) {
        // 数据校验
        if (CharSequenceUtil.isBlank(articleId) || CharSequenceUtil.isBlank(userId)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
        // 调用service 进行人工审核
        articleService.withdrawArticle(articleId, userId);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryAllPageListArticles(Integer status, Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        // 校验status
        if (status != null && !status.equals(Article.STATUS_VERIFYING.type)
                && !status.equals(Article.STATUS_APPROVED.type)
                && !status.equals(Article.STATUS_REJECTED.type)
                && !status.equals(Article.STATUS_RECALLED.type)
                && !status.equals(Article.SELECT_ALL.type)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult = articleService.pageListAllArticles(status, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult queryPageListArticles(String userId, String keyword, Integer status,
                                            Date startDate, Date endDate, Integer page, Integer pageSize) {
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
        if (status != null && !status.equals(Article.STATUS_VERIFYING.type)
                && !status.equals(Article.SELECT_ALL.type)
                && !status.equals(Article.STATUS_APPROVED.type)
                && !status.equals(Article.STATUS_REJECTED.type)
                && !status.equals(Article.STATUS_RECALLED.type)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult = articleService.pageListArticles(userId, keyword, status,
                startDate, endDate, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult deleteArticle(String userId, String articleId) {
        if (CharSequenceUtil.isNotBlank(userId)) {
            if (CharSequenceUtil.isNotBlank(articleId)) {
                articleService.removeArticleByArticleId(userId, articleId);
            } else {
                return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
            }
        } else {
            return JsonResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        return JsonResult.ok();
    }
}
