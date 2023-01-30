package cn.codeprobe.article.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.article.CommentWriterControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.CommentWriterService;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;

/**
 * @author Lionido
 */
@RestController
public class CommentWriterController extends ApiController implements CommentWriterControllerApi {

    @Resource
    private CommentWriterService commentWriterService;

    @Override
    public JsonResult queryPageListComments(String writerId, Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        if (CharSequenceUtil.isBlank(writerId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_COMMENT_LIST_QUERY_FAILED);
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult = commentWriterService.pageListCommentByWriterId(writerId, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult delete(String writerId, String commentId) {
        // 参数校验
        if (CharSequenceUtil.isBlank(writerId) || CharSequenceUtil.isBlank(commentId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.ARTICLE_COMMENT_DELETE_FAILED);
        }
        // 调用 service 删除文章评论
        commentWriterService.removeComment(writerId, commentId);
        return JsonResult.ok();
    }
}
