package cn.codeprobe.api.controller.article;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 门户：文章服务接口
 *
 * @author Lionido
 */
@Api(value = "文章服务接口", tags = "文章服务接口")
@RequestMapping("/portal/article")
public interface ArticlePortalControllerApi {

    /**
     * 创作中心：查询某用户文章分页列表
     *
     * @param userId 用户ID
     * @param keyword 关键词
     * @param status 状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param page 当前页
     * @param pageSize 当前页数量
     * @return PageGridResult
     */
    @ApiOperation(value = "查询文章分页列表", notes = "查询文章分页列表", httpMethod = "POST")
    @PostMapping("/queryPageListArticles")
    JsonResult queryPageListArticles(@RequestParam String userId, String keyword, Integer status, Date startDate,
        Date endDate, Integer page, Integer pageSize);

}
