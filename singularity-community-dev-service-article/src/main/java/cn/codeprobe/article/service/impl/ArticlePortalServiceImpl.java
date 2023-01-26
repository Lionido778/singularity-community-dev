package cn.codeprobe.article.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticlePortalService;
import cn.codeprobe.pojo.po.ArticleDO;
import cn.codeprobe.pojo.vo.ArticleDetailVO;
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
        // 门户统一文章查询条件
        Example.Criteria portalCriteria = getPortalCriteria(example);
        // 关键词查询
        if (CharSequenceUtil.isNotBlank(keyword)) {
            portalCriteria.andLike("title", "%" + keyword + "%");
        }
        // 文章领域
        if (category != null) {
            portalCriteria.andEqualTo("categoryId", category);
        }
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
        // 门户统一文章查询条件
        Example.Criteria portalCriteria = getPortalCriteria(example);
        // 分页查询
        PageHelper.startPage(1, 5);
        List<ArticleDO> articleDOList = articleMapper.selectByExample(example);
        return BeanUtil.copyToList(articleDOList, IndexArticleVO.class);

    }

    @Override
    public PagedGridResult pageListGoodArticlesOfWriter(String writerId, Integer page, Integer pageSize) {
        Example example = new Example(ArticleDO.class);
        Example.Criteria criteria = getPortalCriteria(example);
        // 该用户下的所有文章
        criteria.andEqualTo("publishUserId", writerId);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> articleDOList = articleMapper.selectByExample(example);
        return setterPageGrid(articleDOList, page);
    }

    @Override
    public PagedGridResult pageListArticlesOfWriter(String writerId, Integer page, Integer pageSize) {
        Example example = new Example(ArticleDO.class);
        // 门户统一文章查询条件
        Example.Criteria portalCriteria = getPortalCriteria(example);
        // 该用户下的所有文章
        portalCriteria.andEqualTo("publishUserId", writerId);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> articleDOList = articleMapper.selectByExample(example);
        PagedGridResult pagedGridResult = setterPageGrid(articleDOList, page);
        // 拼接成 indexArticleVOList
        List<IndexArticleVO> indexArticleVOList = getIndexArticleVOList(articleDOList);
        pagedGridResult.setRows(indexArticleVOList);
        return pagedGridResult;
    }

    @Override
    public ArticleDetailVO getArticleDetail(String articleId) {
        ArticleDO articleDO = articleMapper.selectByPrimaryKey(articleId);
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(articleDO, articleDetailVO);
        return articleDetailVO;
    }

}
