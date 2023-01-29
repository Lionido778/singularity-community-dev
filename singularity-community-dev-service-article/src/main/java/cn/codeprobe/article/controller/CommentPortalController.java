package cn.codeprobe.article.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.article.CommentPortalControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.CommentPortalService;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.bo.NewCommentBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class CommentPortalController extends ApiController implements CommentPortalControllerApi {

    @Resource
    private CommentPortalService commentPortalService;

    @Override
    public JsonResult addNewComment(NewCommentBO newCommentBO, BindingResult result) {
        // 校验前端数据
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            JsonResult.errorMap(errors);
        }
        // 调用service 新增评论
        commentPortalService.saveComment(newCommentBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult counts(String articleId) {
        Integer count = 0;
        if (CharSequenceUtil.isNotBlank(articleId)) {
            // 调用service 获取文章总评论数
            count = commentPortalService.countAllCommentsOfArticle(articleId);
        }
        return JsonResult.ok(count);
    }

    @Override
    public JsonResult queryPageList(String articleId, Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        if (CharSequenceUtil.isBlank(articleId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_COMMENT_LIST_QUERY_FAILED);
        }
        // 调用service查询文章列表
        PagedGridResult pagedGridResult = commentPortalService.pageListComments(articleId, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }
}
