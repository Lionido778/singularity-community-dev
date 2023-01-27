package cn.codeprobe.article.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class ArticlePortalServiceImpl extends ArticleBaseService implements ArticlePortalService {

    @Override
    public PagedGridResult pageListArticles(String keyword, Integer category, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
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
        List<Article> articleList = articleMapper.selectByExample(example);
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
        Example.Criteria portalCriteria = getPortalCriteria(example);
        // 分页查询
        PageHelper.startPage(1, 5);
        List<Article> articleList = articleMapper.selectByExample(example);
        return BeanUtil.copyToList(articleList, IndexArticleVO.class);

    }

    @Override
    public PagedGridResult pageListGoodArticlesOfWriter(String writerId, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = getPortalCriteria(example);
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
        Example.Criteria portalCriteria = getPortalCriteria(example);
        // 该用户下的所有文章
        portalCriteria.andEqualTo("publishUserId", writerId);
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
        Example.Criteria portalCriteria = getPortalCriteria(example);
        portalCriteria.andEqualTo("id", articleId);
        Article article = articleMapper.selectOneByExample(example);
        if (article == null) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_VIEW_DETAIL_FAILED);
        }
        // po -> vo
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article, articleDetailVO);

        // 远程调用 user service 通过id查询用户
        String publishUserId = article.getPublishUserId();
        UserBasicInfoVO userBasicInfoVO = null;
        String userServerUrl = "http://writer.codeprobe.cn:8003/writer/user/queryUserBasicInfo?userId=" + publishUserId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(userServerUrl, JsonResult.class);
        if (entity.getStatusCode() == HttpStatus.OK) {
            Object data = Objects.requireNonNull(entity.getBody()).getData();
            String jsonStr = JSONUtil.toJsonStr(data);
            userBasicInfoVO = JSONUtil.toBean(jsonStr, UserBasicInfoVO.class);
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_PUBLISH_USER_ERROR);
        }
        // articleDetailVO 拼接 publishUserName
        articleDetailVO.setPublishUserName(userBasicInfoVO.getNickname());
        return articleDetailVO;
    }

}
