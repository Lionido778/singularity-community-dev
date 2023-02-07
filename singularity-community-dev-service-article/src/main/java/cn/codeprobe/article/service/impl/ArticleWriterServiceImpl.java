package cn.codeprobe.article.service.impl;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticleWriterService;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.pojo.po.Article;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class ArticleWriterServiceImpl extends ArticleBaseService implements ArticleWriterService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void withdrawArticle(String articleId, String userId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        Article pendingArticle = new Article();
        pendingArticle.setArticleStatus(cn.codeprobe.enums.Article.STATUS_RECALLED.type);
        int res = articleMapper.updateByExampleSelective(pendingArticle, example);
        if (!MybatisResult.SUCCESS.result.equals(res)) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
        // 删除对应GridFS中已发布静态文章
        Article article = articleMapper.selectByPrimaryKey(articleId);
        String mongoFileId = article.getMongoFileId();
        if (CharSequenceUtil.isNotBlank(mongoFileId)) {
            gridFsBucket.delete(new ObjectId(mongoFileId));
            // 通过rest 使静态文章消费端删除静态文章HTMl
            // deleteHtml(articleId);
            // 通过 RabbitMQ 使静态文章消费端删除静态文章HTML
            deleteHtmlByMq(articleId);
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publishAppointedArticle() {
        articleMapperCustom.updateAppointToPublish();
    }

    @Override
    public PagedGridResult pageListArticles(String userId, String keyword, Integer status, Date startDate, Date endDate,
        Integer page, Integer pageSize) {

        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        // 关键词查询
        if (CharSequenceUtil.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        // 文章状态
        if (status != null) {
            // 文章状态，如果是 12（审核中） ，同时查询1（机器审核）和2（人工审核）
            articleCriteriaStatus(status, criteria);
        }
        // 起始时间
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createTime", startDate);
        }
        // 结束时间
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createTime", endDate);
        }
        // 非逻辑删除
        criteria.andEqualTo("isDelete", cn.codeprobe.enums.Article.UN_DELETED.type);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveArticle(@NotNull NewArticleBO newArticleBO) {
        Article article = new Article();
        Integer articleType = newArticleBO.getArticleType();
        // 设置文章图文类型
        article.setArticleType(articleType);
        if (articleType.equals(cn.codeprobe.enums.Article.HAS_COVER.type)) {
            // 图文类型，设置文章分面
            article.setArticleCover(newArticleBO.getArticleCover());
        }
        String articleId = idWorker.nextIdStr();
        // 生成文章主键
        article.setId(articleId);
        // 文章标题
        article.setTitle(newArticleBO.getTitle());
        // 文章内容
        article.setContent(newArticleBO.getContent());
        // 文章状态 （文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
        article.setArticleStatus(cn.codeprobe.enums.Article.STATUS_MACHINE_VERIFYING.type);
        // 所属分类
        article.setCategoryId(newArticleBO.getCategoryId());
        // 初始化评论数量
        article.setCommentCounts(cn.codeprobe.enums.Article.INITIAL_COMMENT_COUNTS.type);
        // 初始化阅读数量
        article.setReadCounts(cn.codeprobe.enums.Article.INITIAL_READ_COUNTS.type);
        // 是否删除 （逻辑删除状态，非物理删除，1：删除，0：未删除）
        article.setIsDelete(cn.codeprobe.enums.Article.UN_DELETED.type);
        // 文章发布者
        article.setPublishUserId(newArticleBO.getPublishUserId());
        // 是否预约发布 （是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布 在预约时间到点的时候，把1改为0，则发布
        article.setIsAppoint(newArticleBO.getIsAppoint());
        // 及时发布
        if (newArticleBO.getIsAppoint().equals(cn.codeprobe.enums.Article.UN_APPOINTED.type)
            && newArticleBO.getPublishTime() == null) {
            // 发布时间与创建时间一致
            Date date = new Date();
            article.setPublishTime(date);
            article.setCreateTime(date);
            // 预约发布
        } else if (newArticleBO.getIsAppoint().equals(cn.codeprobe.enums.Article.APPOINTED.type)
            && newArticleBO.getPublishTime() != null) {
            // 文章创建时间
            article.setPublishTime(newArticleBO.getPublishTime());
            article.setCreateTime(newArticleBO.getPublishTime());
        }
        // 文章更新时间
        article.setUpdateTime(new Date());

        // 保存文章到数据库
        int result = articleMapper.insert(article);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
        // 阿里云 AI 文本审核
        scanText(article);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeArticleByArticleId(String userId, String articleId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId).andEqualTo("id", articleId).andEqualTo("isDelete",
            cn.codeprobe.enums.Article.UN_DELETED.type);
        Article pendingArticle = new Article();
        pendingArticle.setIsDelete(cn.codeprobe.enums.Article.DELETED.type);
        int result = articleMapper.updateByExampleSelective(pendingArticle, example);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
        // 删除对应的已发布静态文章
        Article article = articleMapper.selectByPrimaryKey(articleId);
        String mongoFileId = article.getMongoFileId();
        if (CharSequenceUtil.isNotBlank(mongoFileId)) {
            gridFsBucket.delete(new ObjectId(mongoFileId));
            // 通过rest 使静态文章消费端删除静态文章HTMl
            // deleteHtml(articleId);
            // 通过 RabbitMQ 使静态文章消费端删除静态文章HTML
            deleteHtmlByMq(articleId);
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_DELETE_ERROR);

        }
    }
}
