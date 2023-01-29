package cn.codeprobe.api.controller.article;

import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import cn.codeprobe.pojo.bo.NewCommentBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Lionido
 */
@Api(value = "文章评论服务接口", tags = "文章评论服务接口")
@RequestMapping("/portal/comment")
public interface CommentPortalControllerApi {

    /**
     * 门户：新增评论
     *
     * @param newCommentBO 新增文章 BO
     * @param result 校验结果
     * @return JsonResult
     */
    @ApiOperation(value = "用户发表文章评论", notes = "用户发表文章评论", httpMethod = "POST")
    @PostMapping("/addComment")
    JsonResult addNewComment(@RequestBody @Valid NewCommentBO newCommentBO, BindingResult result);

    /**
     * 门户：获取文章评论总数
     *
     * @param articleId 文章 id
     * @return JsonResult
     */
    @ApiOperation(value = "获取文章评论总数", notes = "获取文章评论总数", httpMethod = "GET")
    @GetMapping("/counts")
    JsonResult counts(@RequestParam String articleId);

    /**
     * 门户：获取文章评论列表
     * 
     * @param page 当前页
     * @param pageSize 每页查询数量
     * @param articleId 文章id
     * @return JsonResult
     */
    @ApiOperation(value = "获取文章评论列表", notes = "获取文章评论列表", httpMethod = "GET")
    @GetMapping("/queryPageList")
    JsonResult queryPageList(@RequestParam String articleId, Integer page, Integer pageSize);

}
