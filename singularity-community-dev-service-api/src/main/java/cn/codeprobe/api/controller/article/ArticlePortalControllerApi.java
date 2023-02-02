package cn.codeprobe.api.controller.article;

import org.springframework.web.bind.annotation.GetMapping;
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
     * 门户：查询所有文章分页列表
     *
     * @param keyword 关键词
     * @param category 文章领域
     * @param page 当前页
     * @param pageSize 当前页数量
     * @return PageGridResult
     */
    @ApiOperation(value = "查询文章分页列表", notes = "查询文章分页列表", httpMethod = "GET")
    @GetMapping("/queryPageList")
    JsonResult queryPageList(@RequestParam String keyword, Integer category, Integer page, Integer pageSize);

    /**
     * 门户：查询某热门文章
     * 
     * @return list
     */
    @ApiOperation(value = "查询文章分页列表", notes = "查询文章分页列表", httpMethod = "GET")
    @GetMapping("/queryListHot")
    JsonResult queryListHot();

    /**
     * 门户个人页面：查询某用户文章分页列表
     *
     * @param writerId 作者ID
     * @param page 当前页
     * @param pageSize 当前页数量
     * @return PageGridResult
     */
    @ApiOperation(value = "查询某用户文章分页列表", notes = "查询某用户文章分页列表", httpMethod = "GET")
    @GetMapping("/queryArticlesOfWriter")
    JsonResult queryPageListArticleOfWriter(@RequestParam String writerId, Integer page, Integer pageSize);

    /**
     * 门户个人页面：查询某用户佳作文章分页列表
     *
     * @param writerId 作者ID
     * @return PageGridResult
     */
    @ApiOperation(value = "查询某用户佳作文章分页列表", notes = "查询某用户佳作文章分页列表", httpMethod = "GET")
    @GetMapping("/queryGoodArticlesOfWriter")
    JsonResult queryPageListGoodArticleOfWriter(@RequestParam String writerId);

    /**
     * 门户：查看文章详细内容
     *
     * @param articleId 作者ID
     * @return JsonResult
     */
    @ApiOperation(value = "查看文章详细", notes = "查看文章详细内容", httpMethod = "GET")
    @GetMapping("/detail")
    JsonResult detail(@RequestParam String articleId);

    /**
     * 门户：统计文章阅读数量
     *
     * @param articleId 作者ID
     * @return JsonResult
     */
    @ApiOperation(value = "统计文章阅读数量", notes = "统计文章阅读数量", httpMethod = "POST")
    @PostMapping("/readArticle")
    JsonResult readArticle(@RequestParam String articleId);

    /**
     * 文章静态页面获取文章阅读数量
     *
     * @param articleId 作者ID
     * @return JsonResult
     */
    @ApiOperation(value = "文章静态页面获取文章阅读数量", notes = "文章静态页面获取文章阅读数量", httpMethod = "GET")
    @GetMapping("/readCount")
    JsonResult readCount(@RequestParam String articleId);

}
