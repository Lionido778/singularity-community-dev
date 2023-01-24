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
     * 创作中心：查询某用户文章分页列表
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

    /**
     * 管理中心：查询所有文章分页列表
     *
     * @param status   文章状态
     * @param page     当前页
     * @param pageSize 当前页数量
     * @return PageGridResult
     */
    @ApiOperation(value = "查询所有文章分页列表", notes = "查询所有文章分页列表", httpMethod = "POST")
    @PostMapping("/queryAllPageListArticles")
    JsonResult queryAllPageListArticles(@RequestParam Integer status, Integer page, Integer pageSize);

    /**
     * 逻辑删除文章 1：删除，0: 不删除
     *
     * @param userId    用户ID
     * @param articleId 文章ID
     * @return ok
     */
    @ApiOperation(value = "删除文章", notes = "删除文章", httpMethod = "POST")
    @PostMapping("/deleteArticle")
    JsonResult deleteArticle(@RequestParam String userId, String articleId);

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


    /**
     * 创作中心：撤回文章
     *
     * @param articleId 文章ID
     * @param userId    用户ID
     * @return ok
     */
    @ApiOperation(value = "撤回文章", notes = "撤回文章", httpMethod = "POST")
    @PostMapping("/withdraw")
    JsonResult withdrawArticle(@RequestParam String userId, String articleId);
}
