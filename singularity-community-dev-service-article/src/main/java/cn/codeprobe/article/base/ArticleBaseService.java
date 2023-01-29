package cn.codeprobe.article.base;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;

import cn.codeprobe.article.mapper.ArticleMapper;
import cn.codeprobe.article.mapper.ArticleMapperCustom;
import cn.codeprobe.enums.ContentSecurity;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Article;
import cn.codeprobe.pojo.vo.IndexArticleVO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.RedisUtil;
import cn.codeprobe.utils.ReviewTextUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
public class ArticleBaseService {

    public static final String REDIS_ARTICLE_VIEWS_COUNT = "article_views_count";
    public static final String REDIS_ARTICLE_VIEWED = "article_viewed";
    public static final Long EXPIRED_TIME = (long)24 * 60 * 3600;

    @Resource
    public IdWorker idWorker;
    @Resource
    public ArticleMapper articleMapper;
    @Resource
    public ArticleMapperCustom articleMapperCustom;
    @Resource
    public ReviewTextUtil reviewTextUtil;
    @Resource
    public RestTemplate restTemplate;
    @Resource
    public RedisUtil redisUtil;
    @Resource
    public HttpServletRequest request;

    /**
     * 门户文章列表 查询条件设置
     *
     * @param example example
     * @return Example.Criteria
     */
    @NotNull
    public static Example.Criteria getPortalCommonCriteria(Example example) {
        Example.Criteria criteria = example.createCriteria();
        // 文章必须：非逻辑删除
        criteria.andEqualTo("isDelete", cn.codeprobe.enums.Article.UN_DELETED.type);
        // 文章必须：及时发布
        criteria.andEqualTo("isAppoint", cn.codeprobe.enums.Article.UN_APPOINTED.type);
        // 文章必须：审核通过
        criteria.andEqualTo("articleStatus", cn.codeprobe.enums.Article.STATUS_APPROVED.type);
        return criteria;
    }

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
        Article article = new Article();
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        if (block) {
            article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_REJECTED.type);
        } else if (review) {
            article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_MANUAL_VERIFYING.type);
        } else if (pass) {
            article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_APPROVED.type);
        }
        int result = articleMapper.updateByExampleSelective(article, example);
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
            if (status.equals(cn.codeprobe.enums.Article.STATUS_VERIFYING.type)) {
                criteria.andEqualTo("articleStatus", cn.codeprobe.enums.Article.STATUS_MACHINE_VERIFYING.type)
                    .orEqualTo("articleStatus", cn.codeprobe.enums.Article.STATUS_MANUAL_VERIFYING.type);
            } else if (status.equals(cn.codeprobe.enums.Article.STATUS_APPROVED.type)
                || status.equals(cn.codeprobe.enums.Article.STATUS_REJECTED.type)
                || status.equals(cn.codeprobe.enums.Article.STATUS_RECALLED.type)) {
                criteria.andEqualTo("articleStatus", status);
            }
        }
    }

    /**
     * ArticleDO 拼接 UserBasicInfo 形成 IndexArticleVO 返回给前端
     *
     * @param articleList 原始ArticleDO
     * @return 拼接好的 IndexArticleVO
     */
    public List<IndexArticleVO> getIndexArticleVOList(List<Article> articleList) {
        if (articleList.isEmpty()) {
            return null;
        }
        // 文章发布用户ID列表(去重)
        Set<String> idSet = new HashSet<>();
        // redis 浏览量Key 列表
        List<String> viewsKeyList = new ArrayList<>();
        for (Article article : articleList) {
            String publishUserId = article.getPublishUserId();
            String articleId = article.getId();
            viewsKeyList.add(REDIS_ARTICLE_VIEWS_COUNT + ":" + articleId);
            idSet.add(publishUserId);
        }
        // 批量获取各文章对应的浏览量
        List<Integer> viewsList = getViewsList(viewsKeyList);

        // 远程调用 user service 通过idSetStr查询用户list
        String idSetStr = JSONUtil.toJsonStr(idSet);
        String userServerUrl =
            "http://writer.codeprobe.cn:8003/portal/user/queryUserBasicInfoBySet?userIds=" + idSetStr;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(userServerUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        if (body != null && body.getStatus() == HttpStatus.OK.value() && body.getData() != null) {
            try {
                String jsonStr = (String)body.getData();
                Map map = JSON.parseObject(jsonStr, Map.class);
                // articleList -> indexArticleVOList
                ArrayList<IndexArticleVO> indexArticleVOList = new ArrayList<>();
                for (int i = 0; i < articleList.size(); i++) {
                    // article -> indexArticleVO
                    Article article = articleList.get(i);
                    IndexArticleVO indexArticleVO = new IndexArticleVO();
                    BeanUtils.copyProperties(article, indexArticleVO);
                    // 拼接 userBasicInfoVO
                    UserBasicInfoVO userBasicInfoVO =
                        JSON.to(UserBasicInfoVO.class, map.get(article.getPublishUserId()));
                    if (userBasicInfoVO != null) {
                        indexArticleVO.setPublisherVO(userBasicInfoVO);
                    }
                    // 拼接 ReadCounts
                    Integer views = viewsList.get(i);
                    indexArticleVO.setReadCounts(views);
                    indexArticleVOList.add(indexArticleVO);
                }
                return indexArticleVOList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * redis中获取文章浏览量
     * 
     * @param articleId 文章ID
     * @return views
     */
    @NotNull
    public Integer getViewsOfArticle(String articleId) {
        String value = redisUtil.get(REDIS_ARTICLE_VIEWS_COUNT + ":" + articleId);
        int views;
        if (CharSequenceUtil.isBlank(value)) {
            views = 0;
        } else {
            views = Integer.parseInt(value);
        }
        return views;
    }

    /**
     * 批量获取文章浏览量
     * 
     * @param keys redis key
     * @return viewsIntList
     */
    @NotNull
    public List<Integer> getViewsList(List<String> keys) {
        // mget 批量获取redis中相对应的值（相比较get优化）
        List<String> viewsStrList = redisUtil.mget(keys);
        ArrayList<Integer> viewsIntList = new ArrayList<>();
        for (String views : viewsStrList) {
            int readCount = 0;
            if (CharSequenceUtil.isNotBlank(views)) {
                readCount = Integer.parseInt(views);
            }
            viewsIntList.add(readCount);
        }
        return viewsIntList;
    }

}