package cn.codeprobe.article.service.impl;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticleService;
import cn.codeprobe.enums.Article;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewArticleBO;
import cn.codeprobe.pojo.po.ArticleDO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import com.github.pagehelper.PageHelper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Lionido
 */
@Service
public class ArticleServiceImpl extends ArticleBaseService implements ArticleService {

    @Override
    public void withdrawArticle(String articleId, String userId) {
        Example example = new Example(ArticleDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        int count = articleMapper.selectCountByExample(example);
        if (count >= 1) {
            ArticleDO articleDO = new ArticleDO();
            articleDO.setArticleStatus(Article.STATUS_RECALLED.type);
            int res = articleMapper.updateByExampleSelective(articleDO, example);
            if (!MybatisResult.SUCCESS.result.equals(res)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    @Override
    public void manualReviewArticle(String articleId, Integer passOrNot) {
        boolean isExist = articleMapper.existsWithPrimaryKey(articleId);
        if (isExist) {
            ArticleDO articleDO = new ArticleDO();
            articleDO.setId(articleId);
            if (passOrNot.equals(Article.MANUAL_REVIEW_PASS.type)) {
                articleDO.setArticleStatus(Article.STATUS_APPROVED.type);
            } else if (passOrNot.equals(Article.MANUAL_REVIEW_BLOCK.type)) {
                articleDO.setArticleStatus(Article.STATUS_REJECTED.type);
            }
            int result = articleMapper.updateByPrimaryKeySelective(articleDO);
            if (!MybatisResult.SUCCESS.result.equals(result)) {
                GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
            }
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Override
    public PagedGridResult pageListAllArticles(Integer status, Integer page, Integer pageSize) {
        Example example = new Example(ArticleDO.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        // 文章状态，如果是 12（审核中） ，同时查询1（机器审核）和2（人工审核）
        articleCriteriaStatus(status, criteria);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> list = articleMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void publishAppointedArticle() {
        articleMapperCustom.updateAppointToPublish();
    }

    @Override
    public PagedGridResult pageListArticles(String userId, String keyword, Integer status,
                                            Date startDate, Date endDate, Integer page, Integer pageSize) {

        Example example = new Example(ArticleDO.class);
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
        criteria.andEqualTo("isDelete", Article.UN_DELETED.type);
        // 分页查询
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> list = articleMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveArticle(@NotNull NewArticleBO newArticleBO) {
        ArticleDO articleDO = new ArticleDO();
        Integer articleType = newArticleBO.getArticleType();
        // 设置文章图文类型
        articleDO.setArticleType(articleType);
        if (articleType.equals(Article.HAS_COVER.type)) {
            // 图文类型，设置文章分面
            articleDO.setArticleCover(newArticleBO.getArticleCover());
        }
        String articleId = idWorker.nextIdStr();
        // 生成文章主键
        articleDO.setId(articleId);
        // 文章标题
        articleDO.setTitle(newArticleBO.getTitle());
        // 文章内容
        articleDO.setContent(newArticleBO.getContent());
        // 文章状态 （文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
        articleDO.setArticleStatus(Article.STATUS_MACHINE_VERIFYING.type);
        // 所属分类
        articleDO.setCategoryId(newArticleBO.getCategoryId());
        // 初始化评论数量
        articleDO.setCommentCounts(Article.INITIAL_COMMENT_COUNTS.type);
        // 初始化阅读数量
        articleDO.setReadCounts(Article.INITIAL_READ_COUNTS.type);
        // 是否删除 （逻辑删除状态，非物理删除，1：删除，0：未删除）
        articleDO.setIsDelete(Article.UN_DELETED.type);
        // 文章发布者
        articleDO.setPublishUserId(newArticleBO.getPublishUserId());
        // 是否预约发布  （是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
        articleDO.setIsAppoint(newArticleBO.getIsAppoint());
        // 及时发布
        if (newArticleBO.getIsAppoint().equals(Article.UN_APPOINTED.type) && newArticleBO.getPublishTime() == null) {
            // 发布时间与创建时间一致
            Date date = new Date();
            articleDO.setPublishTime(date);
            articleDO.setCreateTime(date);
            // 预约发布
        } else if (newArticleBO.getIsAppoint().equals(Article.APPOINTED.type) && newArticleBO.getPublishTime() != null) {
            // 文章创建时间
            articleDO.setPublishTime(newArticleBO.getPublishTime());
            articleDO.setCreateTime(newArticleBO.getPublishTime());
        }
        // 文章更新时间
        articleDO.setUpdateTime(new Date());

        // 保存文章到数据库
        int result = articleMapper.insert(articleDO);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
        // 阿里云 AI 文本审核
        scanText(articleId, newArticleBO.getContent());
    }

    @Override
    public void removeArticleByArticleId(String userId, String articleId) {
        Example example = new Example(ArticleDO.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId", userId)
                .andEqualTo("id", articleId)
                .andEqualTo("isDelete", Article.UN_DELETED.type);
        ArticleDO articleDO = new ArticleDO();
        articleDO.setIsDelete(Article.DELETED.type);
        int result = articleMapper.updateByExampleSelective(articleDO, example);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }
}
