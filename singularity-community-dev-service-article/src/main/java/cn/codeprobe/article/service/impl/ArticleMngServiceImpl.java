package cn.codeprobe.article.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticleMngService;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Article;
import cn.codeprobe.pojo.vo.ArticleDetailVO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
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
            Article article = new Article();
            article.setId(articleId);
            if (passOrNot.equals(cn.codeprobe.enums.Article.MANUAL_REVIEW_PASS.type)) {
                article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_APPROVED.type);
            } else if (passOrNot.equals(cn.codeprobe.enums.Article.MANUAL_REVIEW_BLOCK.type)) {
                article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_REJECTED.type);
            }
            int result = articleMapper.updateByPrimaryKeySelective(article);
            if (!MybatisResult.SUCCESS.result.equals(result)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
            }
            // 人工审核通过后生成静态模板
            if (passOrNot.equals(cn.codeprobe.enums.Article.MANUAL_REVIEW_PASS.type)) {
                ArticleDetailVO articleDetailVO = getArticleDetailVO(articleId);
                if (articleDetailVO != null) {
                    // generateHtml(articleDetailVO);
                    generateHtmlToGridFs(articleDetailVO);
                } else {
                    GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
                }
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Override
    public PagedGridResult pageListAllArticles(Integer status, Integer page, Integer pageSize, String keyword,
        Date startDate, Date endDate) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        // 文章状态，如果是 12（审核中） ，同时查询1（机器审核）和2（人工审核）
        articleCriteriaStatus(status, criteria);
        // 关键词查询
        if (CharSequenceUtil.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        // 起始时间
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createTime", startDate);
        }
        // 结束时间
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createTime", endDate);
        }
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }

}
