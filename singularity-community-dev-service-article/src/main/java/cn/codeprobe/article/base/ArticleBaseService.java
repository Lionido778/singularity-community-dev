package cn.codeprobe.article.base;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.github.pagehelper.PageInfo;

import cn.codeprobe.article.mapper.ArticleMapper;
import cn.codeprobe.article.mapper.ArticleMapperCustom;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.ContentSecurity;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.ArticleDO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.ReviewTextUtil;
import tk.mybatis.mapper.entity.Example;

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
     * @param content 文章内容
     */
    public void scanText(String articleId, String content) {

        // 调用 阿里云 AI 文本内容审核工具 ()
        Map<String, String> map = reviewTextUtil.scanText(content);
        boolean block = map.containsValue(ContentSecurity.SUGGESTION_BLOCK.label);
        boolean pass = map.containsValue(ContentSecurity.SUGGESTION_PASS.label);
        boolean review = map.containsValue(ContentSecurity.SUGGESTION_REVIEW.label);
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

    /**
     * 文章状态 查询条件构造
     *
     * @param status 状态
     * @param criteria 条件
     */
    public void articleCriteriaStatus(Integer status, Example.Criteria criteria) {
        if (status != null) {
            if (status.equals(Article.STATUS_VERIFYING.type)) {
                criteria.andEqualTo("articleStatus", Article.STATUS_MACHINE_VERIFYING.type).orEqualTo("articleStatus",
                    Article.STATUS_MANUAL_VERIFYING.type);
            } else if (status.equals(Article.STATUS_APPROVED.type) || status.equals(Article.STATUS_REJECTED.type)
                || status.equals(Article.STATUS_RECALLED.type)) {
                criteria.andEqualTo("articleStatus", status);
            }
        }
    }
}