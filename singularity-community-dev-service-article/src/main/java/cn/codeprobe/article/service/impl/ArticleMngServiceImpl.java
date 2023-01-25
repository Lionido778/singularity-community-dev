package cn.codeprobe.article.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticleMngService;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.ArticleDO;
import cn.codeprobe.result.page.PagedGridResult;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class ArticleMngServiceImpl extends ArticleBaseService implements ArticleMngService {

    @Override
    public void manualReviewArticle(String articleId, Integer passOrNot) {
        boolean isExist = articleMapper.existsWithPrimaryKey(articleId);
        if (isExist) {
            ArticleDO articleDO = new ArticleDO();
            articleDO.setId(articleId);
            if (passOrNot.equals(Article.MANUAL_REVIEW_PASS.type)) {
                articleDO.setArticleStatus(Article.STATUS_APPROVED.type);
            } else if (passOrNot.equals(Article.MANUAL_REVIEW_BLOCK.type)) {
                articleDO.setArticleStatus(Article.STATUS_REJECTED.type);
            }
            int result = articleMapper.updateByPrimaryKeySelective(articleDO);
            if (!MybatisResult.SUCCESS.result.equals(result)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Override
    public PagedGridResult pageListAllArticles(Integer status, Integer page, Integer pageSize) {
        Example example = new Example(ArticleDO.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        // 文章状态，如果是 12（审核中） ，同时查询1（机器审核）和2（人工审核）
        articleCriteriaStatus(status, criteria);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> list = articleMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }

}
