package cn.codeprobe.article.service.impl;

import cn.codeprobe.article.base.ArticleBaseService;
import cn.codeprobe.article.service.ArticleService;
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
    public PagedGridResult pageListUsers(String userId, String keyword, Integer status,
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
            if (status == 12) {
                // 如果时 12 ，同时查询1和2
                criteria.andEqualTo("articleStatus", 1)
                        .orEqualTo("articleStatus", 2);
            } else if (status == 3 || status == 4 || status == 5) {
                criteria.andEqualTo("articleStatus", status);
            }
        }
        // 起始时间
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createTime", startDate);
        }
        // 结束时间
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createTime", endDate);
        }
        PageHelper.startPage(page, pageSize);
        List<ArticleDO> list = articleMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(@NotNull NewArticleBO newArticleBO) {
        ArticleDO articleDO = new ArticleDO();
        Integer articleType = newArticleBO.getArticleType();
        // 设置文章图文类型
        articleDO.setArticleType(articleType);
        if (articleType == 1) {
            // 图文类型，设置文章分面
            articleDO.setArticleCover(newArticleBO.getArticleCover());
        }
        // 生成文章主键
        articleDO.setId(idWorker.nextIdStr());
        // 文章标题
        articleDO.setTitle(newArticleBO.getTitle());
        // 文章内容
        articleDO.setContent(newArticleBO.getContent());
        // 文章状态 （文章状态，1：审核中（用户已提交），2：机审结束，等待人工审核，3：审核通过（已发布），4：审核未通过；5：文章撤回（已发布的情况下才能撤回和删除）
        articleDO.setArticleStatus(1);
        // 所属分类
        articleDO.setCategoryId(newArticleBO.getCategoryId());
        // 初始化评论数量
        articleDO.setCommentCounts(0);
        // 初始化阅读数量
        articleDO.setReadCounts(0);
        // 是否删除 （逻辑删除状态，非物理删除，1：删除，0：未删除）
        articleDO.setIsDelete(0);
        // 文章发布者
        articleDO.setPublishUserId(newArticleBO.getPublishUserId());
        // 是否预约发布  （是否是预约定时发布的文章，1：预约（定时）发布，0：即时发布    在预约时间到点的时候，把1改为0，则发布
        articleDO.setIsAppoint(newArticleBO.getIsAppoint());
        // 及时发布
        if (newArticleBO.getIsAppoint() == 0 && newArticleBO.getPublishTime() == null) {
            // 发布时间与创建时间一致
            Date date = new Date();
            articleDO.setPublishTime(date);
            articleDO.setCreateTime(date);
            // 预约发布
        } else if (newArticleBO.getIsAppoint() == 1 && newArticleBO.getPublishTime() != null) {
            // 文章创建时间
            articleDO.setPublishTime(newArticleBO.getPublishTime());
            articleDO.setCreateTime(newArticleBO.getPublishTime());
        }
        // 文章更新时间
        articleDO.setUpdateTime(new Date());


        // 保存文章到数据库
        int result = articleMapper.insert(articleDO);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
    }
}
