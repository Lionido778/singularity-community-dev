package cn.codeprobe.article.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.article.ArticleMngControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.ArticleMngService;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * 用户文章服务接口
 *
 * @author Lionido
 */
@RestController
public class ArticleMngController extends ApiController implements ArticleMngControllerApi {

    @Resource
    private ArticleMngService articleMngService;

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
        articleMngService.manualReviewArticle(articleId, passOrNot);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryAllPageListArticles(Integer status, Integer page, Integer pageSize, String keyword,
        Date startDate, Date endDate) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        // 校验status
        if (status != null && !status.equals(Article.STATUS_VERIFYING.type)
            && !status.equals(Article.STATUS_APPROVED.type) && !status.equals(Article.STATUS_REJECTED.type)
            && !status.equals(Article.STATUS_RECALLED.type) && !status.equals(Article.SELECT_ALL.type)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_QUERY_PARAMS_ERROR);
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult =
            articleMngService.pageListAllArticles(status, page, pageSize, keyword, startDate, endDate);
        return JsonResult.ok(pagedGridResult);
    }
}
