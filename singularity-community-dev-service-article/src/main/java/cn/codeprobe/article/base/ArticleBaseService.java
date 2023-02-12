package cn.codeprobe.article.base;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;

import cn.codeprobe.enums.ContentSecurity;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.po.Article;
import cn.codeprobe.pojo.vo.ArticleDetailVO;
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
 * 文章服务 共用方法
 * 
 * @author Lionido
 */
public class ArticleBaseService extends CommonService {

    public static final String REDIS_ARTICLE_VIEWS_COUNT = "article_views_count";
    public static final String REDIS_ARTICLE_VIEWED = "article_viewed";
    public static final Long EXPIRED_TIME = (long)24 * 60 * 3600;

    @Resource
    public IdWorker idWorker;
    @Resource
    public ReviewTextUtil reviewTextUtil;
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
     * 分业查询配置
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
     * AI机器审核文本内容
     * 
     * @param content 文章内容
     * @return 文章审核状态
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer reviewContent(String content) {
        // 调用 阿里云 AI 文本内容审核工具
        Map<String, String> map = reviewTextUtil.scanText(content);
        boolean block = map.containsValue(ContentSecurity.SUGGESTION_BLOCK.label);
        boolean pass = map.containsValue(ContentSecurity.SUGGESTION_PASS.label);
        boolean review = map.containsValue(ContentSecurity.SUGGESTION_REVIEW.label);
        if (block) {
            return cn.codeprobe.enums.Article.STATUS_REJECTED.type;
        } else if (review) {
            return cn.codeprobe.enums.Article.STATUS_MANUAL_VERIFYING.type;
        } else if (pass) {
            return cn.codeprobe.enums.Article.STATUS_APPROVED.type;
        } else {
            // 人工审核
            return cn.codeprobe.enums.Article.STATUS_MANUAL_VERIFYING.type;
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

        List<ServiceInstance> clientInstances = discoveryClient.getInstances("SERVICE-USER");
        ServiceInstance serviceInstance = clientInstances.get(0);
        String host = serviceInstance.getHost();
        int port = serviceInstance.getPort();

        String userServerUrl =
            "http://" + host + ":" + port + "/portal/user/queryUserBasicInfoBySet?userIds=" + idSetStr;
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
     * 获取文章浏览量（redis）
     *
     * @param articleId 文章ID
     * @return viewCount
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
     * 批量获取文章浏览量 (redis)
     *
     * @param keys redis keys
     * @return viewsCountList
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

    /**
     * 获取文章详情VO (通过ID)
     *
     * @param articleId 文章ID
     * @return ArticleDetailVO
     */
    public ArticleDetailVO getArticleDetailVO(String articleId) {
        String articleServiceUrl = "http://www.codeprobe.cn:8001/portal/article/detail?articleId=" + articleId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(articleServiceUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        ArticleDetailVO articleDetailVO = null;
        if (body != null && body.getStatus() == HttpStatus.OK.value() && body.getData() != null) {
            Object data = body.getData();
            String jsonStr = JSONUtil.toJsonStr(data);
            articleDetailVO = JSONUtil.toBean(jsonStr, ArticleDetailVO.class);
        }
        return articleDetailVO;
    }

    /**
     * 发布预约文章 （审核时间超过了预约发布时间调用）
     * 
     * @param articleId 文章ID
     */
    public void publishArticle(String articleId) {
        // 将文章从预约发布状态更新为及时发布
        Article article = new Article();
        article.setId(articleId);
        article.setIsAppoint(cn.codeprobe.enums.Article.UN_APPOINTED.type);
        int result = articleMapper.updateByPrimaryKeySelective(article);
        if (MybatisResult.SUCCESS.result != result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_APPOINT_PUBLISH_FAILED);
        }
        // 生成静态页面HTML
        createHtml(articleId);
    }

    /**
     * 创建静态页面HTML（人工审核通过时调用）
     * 
     * @param articleId 文章ID
     */
    public void createHtml(String articleId) {
        ArticleDetailVO articleDetailVO = getArticleDetailVO(articleId);
        if (articleDetailVO != null) {
            // 生成HTML上传至GridFS
            String mongoFileId = generateHtmlToGridFs(articleDetailVO);
            // 使用 RabbitMQ 发送下载消息让消费端下载静态文章HTML
            scheduleService.produceDownloadHtml(articleDetailVO.getId(), mongoFileId);
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
        }
    }

    /**
     * 创建静态页面HTML （新建文章机审通过时调用）
     *
     * @param article 文章
     */
    public void createHtml(Article article) {
        ArticleDetailVO articleDetailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(article, articleDetailVO);
        UserBasicInfoVO basicInfoVO = getBasicUserInfoById(article.getPublishUserId());
        String nickname = basicInfoVO.getNickname();
        articleDetailVO.setPublishUserName(nickname);
        // 生成HTML上传至GridFS
        String mongoFileId = generateHtmlToGridFs(articleDetailVO);
        // 使用 RabbitMQ 发送下载消息让消费端下载静态文章HTML
        scheduleService.produceDownloadHtml(articleDetailVO.getId(), mongoFileId);
    }

    /**
     * 创建文章定时发布任务
     * 
     * @param articleId 文章ID
     * @param publishTime 发布时间
     */
    public void createPublishSchedule(String articleId, Date publishTime) {
        scheduleService.producePublishAppointedArticle(articleId, publishTime);
    }

}