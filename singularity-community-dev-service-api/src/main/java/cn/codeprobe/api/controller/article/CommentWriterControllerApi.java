package cn.codeprobe.api.controller.article;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Lionido
 */
@Api(value = "文章评论服务接口", tags = "文章评论服务接口")
@RequestMapping("/writer/comment")
public interface CommentWriterControllerApi {

    /**
     * 创作中心：查询作家所有评论
     *
     * @param writerId 作家ID
     * @param page 当前夜
     * @param pageSize 每页查询数量
     * @return JsonResult
     */
    @ApiOperation(value = "查询作家所有评论", notes = "查询作家所有评论", httpMethod = "POST")
    @PostMapping("/queryPageListComments")
    JsonResult queryPageListComments(@RequestParam String writerId, Integer page, Integer pageSize);

    /**
     * 创作中心：删除某条评论
     *
     * @param writerId 作家ID
     * @param commentId 评论ID
     * @return JsonResult
     */
    @ApiOperation(value = "查询作家所有评论", notes = "查询作家所有评论", httpMethod = "POST")
    @PostMapping("/delete")
    JsonResult delete(@RequestParam String writerId, String commentId);

}
