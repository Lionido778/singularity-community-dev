package cn.codeprobe.article.base;

import cn.codeprobe.article.mapper.ArticleMapper;
import cn.codeprobe.article.mapper.ArticleMapperCustom;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.ArticleDO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.ReviewTextUtil;
import com.github.pagehelper.PageInfo;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Lionido
 */
public class ArticleBaseService {

    @Resource
    public IdWorker idWorker;
    @Resource
    public ArticleMapper articleMapper;
    @Resource
    public ArticleMapperCustom articleMapperCustom;
    @Resource
    public ReviewTextUtil reviewTextUtil;


    /**
     * 查询分页配置
     *
     * @param list 查询数据（每页）
     * @param page 当前页
     * @return 封装分页查询结果
     */
    public PagedGridResult setterPageGrid(List<?> list, int page) {
        PageInfo<?> pageInfo = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setTotal(pageInfo.getPages());
        gridResult.setRecords(pageInfo.getTotal());
        return gridResult;
    }

    /**
     * 文章文本内容审核
     *
     * @param articleId 文章ID
     * @param content   文章内容
     */
    public void scanText(String articleId, String content) {
        Map<String, String> map = reviewTextUtil.scanText(content, "ad");
        boolean block = map.containsValue("block");
        boolean pass = map.containsValue("pass");
        boolean review = map.containsValue("review");
        ArticleDO articleDO = new ArticleDO();
        Example example = new Example(ArticleDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        if (block) {
            articleDO.setArticleStatus(Article.STATUS_REJECTED.type);
        } else if (review) {
            articleDO.setArticleStatus(Article.STATUS_MANUAL_VERIFYING.type);
        } else if (pass) {
            articleDO.setArticleStatus(Article.STATUS_APPROVED.type);
        }
        int result = articleMapper.updateByExampleSelective(articleDO, example);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }
}