package cn.codeprobe.article.base;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

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
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
public class ArticleBaseService {

    public static final String REDIS_ARTICLE_VIEWS = "article_views";
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
    public static Example.Criteria getPortalCriteria(Example example) {
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
    @NotNull
    public List<IndexArticleVO> getIndexArticleVOList(List<Article> articleList) {
        // 通过Set 获得 文章列表的所有去重发布用户id
        Set<String> idSet = new HashSet<>();
        for (Article article : articleList) {
            String publishUserId = article.getPublishUserId();
            idSet.add(publishUserId);
        }
        // 远程调用 user service 通过idSet查询用户list
        HashMap<String, UserBasicInfoVO> map = new HashMap<>(0);
        for (String id : idSet) {
            String userServerUrl = "http://writer.codeprobe.cn:8003/writer/user/queryUserBasicInfo?userId=" + id;
            ResponseEntity<JsonResult> entity = restTemplate.getForEntity(userServerUrl, JsonResult.class);
            if (entity.getStatusCode() == HttpStatus.OK) {
                Object data = Objects.requireNonNull(entity.getBody()).getData();
                String jsonStr = JSONUtil.toJsonStr(data);
                UserBasicInfoVO userBasicInfoVO = JSONUtil.toBean(jsonStr, UserBasicInfoVO.class);
                map.put(id, userBasicInfoVO);
            } else {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_PUBLISH_USER_ERROR);
            }
        }
        List<IndexArticleVO> articleVOList = BeanUtil.copyToList(articleList, IndexArticleVO.class);

        // 将 userBasicInfoVO 插入 articleVO
        for (IndexArticleVO articleVO : articleVOList) {
            UserBasicInfoVO userBasicInfoVO = map.get(articleVO.getPublishUserId());
            if (userBasicInfoVO != null) {
                articleVO.setPublisherVO(userBasicInfoVO);
            }
        }
        return articleVOList;
    }

}