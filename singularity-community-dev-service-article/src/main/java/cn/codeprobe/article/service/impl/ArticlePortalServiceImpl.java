package cn.codeprobe.article.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticlePortalService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Article;
import cn.codeprobe.pojo.vo.ArticleDetailVO;
import cn.codeprobe.pojo.vo.IndexArticleVO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.IpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class ArticlePortalServiceImpl extends ArticleBaseService implements ArticlePortalService {

    @Override
    public PagedGridResult pageListIndexArticles(String keyword, Integer category, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        // 门户统一文章查询条件
        Example.Criteria portalCriteria = getPortalCommonCriteria(example);
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
        List<Article> articleList = articleMapper.selectByExample(example);
        if (articleList.isEmpty()) {
            return new PagedGridResult();
        }
        // 分页封装
        PagedGridResult pagedGridResult = setterPageGrid(articleList, page);
        // 拼接 IndexArticleVO
        List<IndexArticleVO> articleVOList = getIndexArticleVOList(articleList);
        pagedGridResult.setRows(articleVOList);
        return pagedGridResult;
    }

    @Override
    public List<IndexArticleVO> pageListHotArticles() {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        // 门户统一文章查询条件
        getPortalCommonCriteria(example);
        // 分页查询
        PageHelper.startPage(1, 5);
        List<Article> articleList = articleMapper.selectByExample(example);
        return BeanUtil.copyToList(articleList, IndexArticleVO.class);

    }

    @Override
    public PagedGridResult pageListGoodArticlesOfWriter(String writerId, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = getPortalCommonCriteria(example);
        // 该用户下的所有文章
        criteria.andEqualTo("publishUserId", writerId);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<Article> articleList = articleMapper.selectByExample(example);
        return setterPageGrid(articleList, page);
    }

    @Override
    public PagedGridResult pageListArticlesOfWriter(String writerId, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        // 门户统一文章查询条件
        Example.Criteria criteria = getPortalCommonCriteria(example);
        // 该用户下的所有文章
        criteria.andEqualTo("publishUserId", writerId);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<Article> articleList = articleMapper.selectByExample(example);
        PagedGridResult pagedGridResult = setterPageGrid(articleList, page);
        // 拼接成 indexArticleVOList
        List<IndexArticleVO> indexArticleVOList = getIndexArticleVOList(articleList);
        pagedGridResult.setRows(indexArticleVOList);
        return pagedGridResult;
    }

    @Override
    public ArticleDetailVO getArticleDetail(String articleId) {
        Example example = new Example(Article.class);
        // 门户统一文章查询条件
        Example.Criteria criteria = getPortalCommonCriteria(example);
        criteria.andEqualTo("id", articleId);
        Article article = articleMapper.selectOneByExample(example);
        if (article == null) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_VIEW_DETAIL_FAILED);
        }
        // po -> vo
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article, articleDetailVO);
        // 这里文章封面需要特殊处理
        articleDetailVO.setCover(article.getArticleCover());
        // 远程调用 user service 通过id查询用户
        String publishUserId = article.getPublishUserId();
        UserBasicInfoVO userBasicInfoVO = getBasicUserInfoById(publishUserId);
        if (userBasicInfoVO == null) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_VIEW_DETAIL_FAILED);
        }
        // articleDetailVO 拼接 publishUserName(发布者昵称) 和 ReadCounts(浏览量)
        String nickname = userBasicInfoVO.getNickname();
        articleDetailVO.setPublishUserName(nickname);
        Integer views = getViewsOfArticle(articleId);
        articleDetailVO.setReadCounts(views);
        return articleDetailVO;
    }

    @Override
    public void countArticleView(String articleId) {
        // 判断articleId是否有效
        Article article = new Article();
        article.setId(articleId);
        int count = articleMapper.selectCount(article);
        if (count >= 0) {
            // 获取请求IP，防刷浏览量
            String requestIp = IpUtil.getRequestIp(request);
            boolean isExist = redisUtil.keyIsExist(REDIS_ARTICLE_VIEWED + ":" + requestIp + ":" + articleId);
            if (!isExist) {
                // view ++
                redisUtil.increment(REDIS_ARTICLE_VIEWS_COUNT + ":" + articleId, 1);
                // 防刷限制时间为24小时
                redisUtil.set(REDIS_ARTICLE_VIEWED + ":" + requestIp + ":" + articleId, articleId, EXPIRED_TIME);
            }
        }
    }

    @Override
    public String getReadCountOfArticle(String articleId) {
        return redisUtil.get(REDIS_ARTICLE_VIEWS_COUNT + ":" + articleId);
    }
}
