package cn.codeprobe.article.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.article.base.CommentBaseService;
import cn.codeprobe.article.service.CommentPortalService;
import cn.codeprobe.enums.MybatisResult;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewCommentBO;
import cn.codeprobe.pojo.po.Comment;
import cn.codeprobe.pojo.vo.ArticleDetailVO;
import cn.codeprobe.pojo.vo.CommentVO;
import cn.codeprobe.pojo.vo.UserBasicInfoVO;
import cn.codeprobe.result.JsonResult;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONUtil;

/**
 * @author Lionido
 */
@Service
public class CommentPortalServiceImpl extends CommentBaseService implements CommentPortalService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveComment(NewCommentBO newCommentBO) {
        String id = idWorker.nextIdStr();
        Comment comment = new Comment();
        comment.setId(id);
        comment.setFatherId(newCommentBO.getFatherId());
        comment.setContent(newCommentBO.getContent());
        // 获取文章 articleDetail
        String articleId = newCommentBO.getArticleId();
        ArticleDetailVO articleDetail = articlePortalService.getArticleDetail(articleId);
        // 获取评论人基本信息
        String commentUserId = newCommentBO.getCommentUserId();
        String userServerUrl = "http://writer.codeprobe.cn:8003/portal/user/queryUserBasicInfo?userId=" + commentUserId;
        ResponseEntity<JsonResult> entity = restTemplate.getForEntity(userServerUrl, JsonResult.class);
        JsonResult body = entity.getBody();
        UserBasicInfoVO userBasicInfoVO = null;
        if (body != null && body.getData() != null && body.getStatus() == HttpStatus.OK.value()) {
            Object data = body.getData();
            String jsonStr = JSONUtil.toJsonStr(data);
            userBasicInfoVO = JSONUtil.toBean(jsonStr, UserBasicInfoVO.class);
        }
        if (articleDetail == null || userBasicInfoVO == null) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_COMMENT_FAILED);
        }
        comment.setArticleId(articleDetail.getId());
        comment.setWriterId(articleDetail.getPublishUserId());
        comment.setArticleTitle(articleDetail.getTitle());
        comment.setArticleCover(articleDetail.getCover());
        comment.setCommentUserId(userBasicInfoVO.getId());
        comment.setCommentUserFace(userBasicInfoVO.getFace());
        comment.setCommentUserNickname(userBasicInfoVO.getNickname());
        comment.setCreateTime(new Date());
        // 插入数据库
        int result = commentMapper.insert(comment);
        if (result != MybatisResult.SUCCESS.result) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ARTICLE_COMMENT_FAILED);
        }
        // redis 文章总评论数+1
        redisUtil.increment(REDIS_ARTICLE_COMMENTS + ":" + articleId, 1);
    }

    @Override
    public Integer countAllCommentsOfArticle(String articleId) {
        String countStr = redisUtil.get(REDIS_ARTICLE_COMMENTS + ":" + articleId);
        int countInt = 0;
        if (CharSequenceUtil.isNotBlank(countStr)) {
            countInt = Integer.parseInt(countStr);
        }
        return countInt;
    }

    @Override
    public PagedGridResult pageListComments(String articleId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CommentVO> commentVOList = commentMapperCustom.selectAllByArticleId(articleId);
        return setterPageGrid(commentVOList, page);
    }
}
