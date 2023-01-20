package cn.codeprobe.article.controller;

import cn.codeprobe.api.controller.article.ArticleControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.ArticleService;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 用户文章服务接口
 *
 * @author Lionido
 */
@RestController
public class ArticleController extends ApiController implements ArticleControllerApi {

    @Resource
    private ArticleService articleService;

    @Override
    public JsonResult addNewArticle(NewArticleBO newArticleBO, BindingResult result) {
        // 校验BO数据
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JsonResult.errorMap(errorMap);
        }
        // 调用 service 执行发文
        articleService.saveArticle(newArticleBO);
        return JsonResult.ok();
    }

    @Override
    public JsonResult queryPageListArticles(String userId, String keyword, Integer status,
                                            Date startDate, Date endDate, Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        if (CharSequenceUtil.isBlank(userId)) {
            return JsonResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult = articleService.pageListUsers(userId, keyword, status,
                startDate, endDate, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }
}
