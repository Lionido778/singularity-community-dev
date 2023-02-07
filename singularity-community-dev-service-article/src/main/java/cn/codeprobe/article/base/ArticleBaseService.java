package cn.codeprobe.article.base;

import java.io.*;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.GridFSBucket;

import cn.codeprobe.api.config.RabbitMq;
import cn.codeprobe.article.mapper.ArticleMapper;
import cn.codeprobe.article.mapper.ArticleMapperCustom;
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
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
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
    @Resource
    public GridFSBucket gridFsBucket;
    @Value("${freemarker.html.target}")
    private String htmlTarget;
    @Resource
    private RabbitTemplate rabbitTemplate;

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
     * @param article 文章
     */
    @Transactional(rollbackFor = Exception.class)
    public void scanText(Article article) {
        String content = article.getContent();
        // 调用 阿里云 AI 文本内容审核工具 ()
        Map<String, String> map = reviewTextUtil.scanText(content);
        boolean block = map.containsValue(ContentSecurity.SUGGESTION_BLOCK.label);
        boolean pass = map.containsValue(ContentSecurity.SUGGESTION_PASS.label);
        boolean review = map.containsValue(ContentSecurity.SUGGESTION_REVIEW.label);
        if (block) {
            article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_REJECTED.type);
        } else if (review) {
            article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_MANUAL_VERIFYING.type);
        } else if (pass) {
            article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_APPROVED.type);
        }
        int result = articleMapper.updateByPrimaryKeySelective(article);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        // AI审核通过后生成静态模板并保存至GridFS
        if (article.getArticleStatus().equals(cn.codeprobe.enums.Article.STATUS_APPROVED.type)) {
            ArticleDetailVO articleDetailVO = new ArticleDetailVO();
            UserBasicInfoVO userBasicInfoVO = getBasicUserInfoById(article.getPublishUserId());
            if (userBasicInfoVO != null) {
                BeanUtils.copyProperties(article, articleDetailVO);
                articleDetailVO.setPublishUserName(userBasicInfoVO.getNickname());
            }
            // generateHtml(articleDetailVO);
            generateHtmlToGridFs(articleDetailVO);
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

    /**
     * 通过ID 获取文章详情VO
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
     * 文章生成静态页面
     *
     * @param articleDetailVO 文章详情VO
     */
    public void generateHtml(ArticleDetailVO articleDetailVO) {
        // 获得动态数据 ArticleDetailVO
        try {
            // 配置freemarker基本环境
            Configuration cfg = new Configuration(Configuration.getVersion());
            // 声明freemarker模板所需要加载的目录的位置 (classpath:/templates)
            String classpath = this.getClass().getResource("/").getPath();
            System.out.println(classpath);
            cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));
            // eg:
            // D:/WorkSpace/ideaProjects/singularity-community-dev/singularity-community-dev-service-article/target/classes/templates
            // 获得现有的模板ftl文件
            Template template = cfg.getTemplate("detail.ftl", "utf-8");
            HashMap<String, Object> model = new HashMap<>(1);
            model.put("articleDetail", articleDetailVO);
            Writer out = new FileWriter(htmlTarget + File.separator + articleDetailVO.getId() + ".html");
            // 融合动态数据和ftl，生成html
            File tempDic = new File(htmlTarget);
            if (!tempDic.exists()) {
                tempDic.mkdirs();
            }
            template.process(model, out);
            // close
            out.close();
        } catch (IOException | TemplateException e) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
        }
    }

    /**
     * 生成的静态文章页面上传至GridFS
     *
     * @param articleDetailVO 文章详情VO
     */
    @Transactional(rollbackFor = Exception.class)
    public void generateHtmlToGridFs(ArticleDetailVO articleDetailVO) {
        // 获得动态数据 ArticleDetailVO
        try {
            // 配置freemarker基本环境
            Configuration cfg = new Configuration(Configuration.getVersion());
            // 声明freemarker模板所需要加载的目录的位置 (classpath:/templates)
            String classpath = this.getClass().getResource("/").getPath();
            System.out.println(classpath);
            cfg.setDirectoryForTemplateLoading(new File(classpath + "templates"));
            // eg:
            // D:/WorkSpace/ideaProjects/singularity-community-dev/singularity-community-dev-service-article/target/classes/templates
            // 获得现有的模板ftl文件
            Template template = cfg.getTemplate("detail.ftl", "utf-8");
            // 动态数据
            HashMap<String, Object> model = new HashMap<>(1);
            model.put("articleDetail", articleDetailVO);
            // 静态页面HTML,上传至GridFS
            String htmlContent = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            InputStream inputStream = IOUtils.toInputStream(htmlContent);
            ObjectId objectId = gridFsBucket.uploadFromStream(articleDetailVO.getId() + ".html", inputStream);
            // 关联至数据库文章表
            String mongoId = objectId.toString();
            Article article = new Article();
            article.setId(articleDetailVO.getId());
            article.setMongoFileId(mongoId);
            int result = articleMapper.updateByPrimaryKeySelective(article);
            if (result != MybatisResult.SUCCESS.result) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
            }
            // 通过 rest 调用让静态文章消费端下载HTML（不利于并行 publishHtml(articleDetailVO.getId(), mongoId);
            // 使用 RabbitMQ 发送下载消息让静态文章消费端下载HTML
            publishHtmlByMq(articleDetailVO.getId(), mongoId);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_FAILED);
        }
    }

    /**
     * 获取用户基本信息
     * 
     * @param userId 用户ID
     * @return UserBasicInfoVO
     */
    public UserBasicInfoVO getBasicUserInfoById(String userId) {
        String userServiceUrl = "http://www.codeprobe.cn:8003/writer/user/queryUserBasicInfo?userId=" + userId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(userServiceUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        UserBasicInfoVO userBasicInfoVO = null;
        if (body != null && body.getStatus() == HttpStatus.OK.value() && body.getData() != null) {
            Object data = body.getData();
            String jsonStr = JSONUtil.toJsonStr(data);
            userBasicInfoVO = JSONUtil.toBean(jsonStr, UserBasicInfoVO.class);
        }
        return userBasicInfoVO;
    }

    /**
     * 通过 rest 让消费端下载HTML
     * 
     * @param articleId 文章ID
     * @param mongoId mongoId
     */
    public void publishHtml(String articleId, String mongoId) {
        String markerServiceUrl =
            "http://www.codeprobe.cn:8002/marker/file/publishHtml?articleId=" + articleId + "&mongoId=" + mongoId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(markerServiceUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        if (body == null || body.getStatus() != HttpStatus.OK.value() || body.getData() == null
            || !Objects.equals(body.getData().toString(), HttpStatus.OK.toString())) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_PUBLISH_FAILED);
        }
    }

    /**
     * 通过RabbitMQ 发送消息 让消费端下载HTML
     * 
     * @param articleId 文章ID
     * @param mongoId mongoId
     */
    public void publishHtmlByMq(String articleId, String mongoId) {
        HashMap<String, String> map = new HashMap<>(5);
        map.put("articleId", articleId);
        map.put("mongoId", mongoId);
        String jsonStr = JSONUtil.toJsonStr(map);
        rabbitTemplate.convertAndSend(RabbitMq.EXCHANGE_ARTICLE, "article.download.do", jsonStr);
    }

    /**
     * 通过 rest 让消费端删除HTML
     * 
     * @param articleId 文章ID
     */
    public void deleteHtml(String articleId) {
        String markerServiceUrl = "http://www.codeprobe.cn:8002/marker/file/deleteHtml?articleId=" + articleId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(markerServiceUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        if (body == null || body.getStatus() != HttpStatus.OK.value() || body.getData() == null
            || !Objects.equals(body.getData().toString(), HttpStatus.OK.toString())) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_STATIC_DELETE_FAILED);
        }
    }

    /**
     * 通过RabbitMQ 发送消息 让消费端删除HTML
     *
     * @param articleId 文章ID
     */
    public void deleteHtmlByMq(String articleId) {
        HashMap<String, String> map = new HashMap<>(5);
        map.put("articleId", articleId);
        String jsonStr = JSONUtil.toJsonStr(map);
        rabbitTemplate.convertAndSend(RabbitMq.EXCHANGE_ARTICLE, "article.delete.do", jsonStr);
    }

}