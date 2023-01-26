package cn.codeprobe.article.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticlePortalService;
import cn.codeprobe.enums.Article;
import cn.codeprobe.pojo.po.ArticleDO;
import cn.codeprobe.pojo.vo.IndexArticleVO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class ArticlePortalServiceImpl extends ArticleBaseService implements ArticlePortalService {

    @Override
    public PagedGridResult pageListArticles(String keyword, Integer category, Integer page, Integer pageSize) {

        Example example = new Example(ArticleDO.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        // 关键词查询
        if (CharSequenceUtil.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        // 文章领域
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }
        // 文章必须：非逻辑删除
        criteria.andEqualTo("isDelete", Article.UN_DELETED.type);
        // 文章必须：及时发布
        criteria.andEqualTo("isAppoint", Article.UN_APPOINTED.type);
        // 文章必须：审核通过
        criteria.andEqualTo("articleStatus", Article.STATUS_APPROVED.type);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> articleDOList = articleMapper.selectByExample(example);
        // 分页封装
        PagedGridResult pagedGridResult = setterPageGrid(articleDOList, page);
        // 拼接 IndexArticleVO
        List<IndexArticleVO> articleVOList = getIndexArticleVOList(articleDOList);
        pagedGridResult.setRows(articleVOList);
        return pagedGridResult;
    }

    @Override
    public List<IndexArticleVO> pageListHotArticles() {
        Example example = new Example(ArticleDO.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        // 文章必须：非逻辑删除
        criteria.andEqualTo("isDelete", Article.UN_DELETED.type);
        // 文章必须：及时发布
        criteria.andEqualTo("isAppoint", Article.UN_APPOINTED.type);
        // 文章必须：审核通过
        criteria.andEqualTo("articleStatus", Article.STATUS_APPROVED.type);
        // 分页查询
        PageHelper.startPage(1, 5);
        List<ArticleDO> articleDOList = articleMapper.selectByExample(example);
        return BeanUtil.copyToList(articleDOList, IndexArticleVO.class);

    }

}
