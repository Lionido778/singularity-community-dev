package cn.codeprobe.api.controller.article;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 管理中心：文章服务接口
 *
 * @author Lionido
 */
@Api(value = "文章服务接口", tags = "文章服务接口")
@RequestMapping("/admin/articleMng")
public interface ArticleMngControllerApi {

    /**
     * 管理中心：查询所有文章分页列表
     *
     * @param keyword 关键词
     * @param status 状态
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param page 当前页
     * @param pageSize 当前页数量
     * @return PageGridResult
     */
    @ApiOperation(value = "查询所有文章分页列表", notes = "查询所有文章分页列表", httpMethod = "POST")
    @PostMapping("/queryAllPageListArticles")
    JsonResult queryAllPageListArticles(@RequestParam Integer status, Integer page, Integer pageSize, String keyword,
        Date startDate, Date endDate);

    /**
     * 管理中心：审核文章 1：通过，0: 不通过
     *
     * @param articleId 文章ID
     * @param passOrNot pass or not
     * @return ok
     */
    @ApiOperation(value = "管理中心审核文章", notes = "管理中心审核文章", httpMethod = "POST")
    @PostMapping("/doReview")
    JsonResult reviewArticle(@RequestParam String articleId, Integer passOrNot);

}
