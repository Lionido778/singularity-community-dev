package cn.codeprobe.article.service;

import java.util.List;

import cn.codeprobe.pojo.vo.ArticleDetailVO;
import cn.codeprobe.pojo.vo.IndexArticleVO;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * 门户：文章服务
 *
 * @author Lionido
 */
public interface ArticlePortalService {

    /**
     * 门户：查询文章列表
     *
     * @param keyword 关键词
     * @param page 当前页
     * @param category 文章领域
     * @param pageSize 每页数量
     * @return 文章分页列表
     */
    PagedGridResult pageListArticles(String keyword, Integer category, Integer page, Integer pageSize);

    /**
     * 门户：获取热门文章列表
     * 
     * @return List<IndexArticleVO>
     */
    List<IndexArticleVO> pageListHotArticles();

    /**
     * 门户个人页面:查询用户文章列表
     * 
     * @param writerId 作者Id
     * @param page 当前页
     * @param pageSize 每页查询数量
     * @return PagedGridResult
     */
    PagedGridResult pageListArticlesOfWriter(String writerId, Integer page, Integer pageSize);

    /**
     * 门户个人页面：查询某用户佳作文章分页列表
     *
     * @param page 当前页
     * @param pageSize 每页查询数量
     * @param writerId 作者ID
     * @return PagedGridResult
     */
    PagedGridResult pageListGoodArticlesOfWriter(String writerId, Integer page, Integer pageSize);

    /**
     * 获取文章
     * 
     * @param articleId 文章ID
     * @return ArticleDetailVO
     */
    ArticleDetailVO getArticleDetail(String articleId);

    /**
     * 统计文章浏览量
     * 
     * @param articleId 文章ID
     */
    void countArticleView(String articleId);
}
