package cn.codeprobe.api.controller.article;

import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.result.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

/**
 * 文章服务接口
 *
 * @author Lionido
 */
@Api(value = "文章服务接口", tags = "文章服务接口")
@RequestMapping("/article")
public interface ArticleControllerApi {

    /**
     * 创作中心
     * 创建新文章
     *
     * @param newArticleBO 新增文章 BO
     * @param result       校验结果
     * @return result
     */
    @ApiOperation(value = "用户新增文章", notes = "用户新增文章", httpMethod = "POST")
    @PostMapping("/addNewArticle")
    JsonResult addNewArticle(@RequestBody @Valid NewArticleBO newArticleBO, BindingResult result);

    /**
     * 查询文章分页列表
     *
     * @param userId    用户ID
     * @param keyword   关键词
     * @param status    状态
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param page      当前页
     * @param pageSize  当前页数量
     * @return PageGridResult
     */
    @ApiOperation(value = "查询文章分页列表", notes = "查询文章分页列表", httpMethod = "POST")
    @PostMapping("/queryPageListArticles")
    JsonResult queryPageListArticles(@RequestParam
                                     String userId, String keyword,
                                     Integer status, Date startDate,
                                     Date endDate, Integer page, Integer pageSize);

}
