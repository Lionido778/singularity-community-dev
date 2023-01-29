package cn.codeprobe.article.base;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.client.RestTemplate;

import com.github.pagehelper.PageInfo;

import cn.codeprobe.article.mapper.CommentMapper;
import cn.codeprobe.article.mapper.CommentMapperCustom;
import cn.codeprobe.article.service.ArticlePortalService;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.IdWorker;
import cn.codeprobe.utils.RedisUtil;

/**
 * @author Lionido
 */
public class CommentBaseService {

    public static final String REDIS_ARTICLE_COMMENTS = "article_comment_count";

    @Resource
    public IdWorker idWorker;
    @Resource
    public ArticlePortalService articlePortalService;
    @Resource
    public RestTemplate restTemplate;
    @Resource
    public RedisUtil redisUtil;
    @Resource
    public CommentMapper commentMapper;
    @Resource
    public CommentMapperCustom commentMapperCustom;

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

}