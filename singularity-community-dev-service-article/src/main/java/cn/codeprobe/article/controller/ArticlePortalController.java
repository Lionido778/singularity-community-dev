package cn.codeprobe.article.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RestController;

import cn.codeprobe.api.controller.article.ArticlePortalControllerApi;
import cn.codeprobe.api.controller.base.ApiController;
import cn.codeprobe.article.service.ArticlePortalService;
import cn.codeprobe.enums.PageHelper;
import cn.codeprobe.pojo.vo.IndexArticleVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * 用户文章服务接口
 *
 * @author Lionido
 */
@RestController
public class ArticlePortalController extends ApiController implements ArticlePortalControllerApi {

    @Resource
    private ArticlePortalService articlePortalService;

    @Override
    public JsonResult queryPageList(String keyword, Integer category, Integer page, Integer pageSize) {
        // 校验数据并初始化
        if (page == null) {
            page = PageHelper.DEFAULT_PAGE.page;
        }
        if (pageSize == null) {
            pageSize = PageHelper.DEFAULT_PAGE.pageSize;
        }
        // 调用 service 查询文章列表
        PagedGridResult pagedGridResult = articlePortalService.pageListArticles(keyword, category, page, pageSize);
        return JsonResult.ok(pagedGridResult);
    }

    @Override
    public JsonResult queryListHot() {
        // 调用 service 查询文章列表
        List<IndexArticleVO> articleVOList = articlePortalService.pageListHotArticles();
        return JsonResult.ok(articleVOList);
    }

}
