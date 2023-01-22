package cn.codeprobe.article.base;

import cn.codeprobe.article.mapper.ArticleMapper;
import cn.codeprobe.article.mapper.ArticleMapperCustom;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.utils.IdWorker;
import com.github.pagehelper.PageInfo;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Lionido
 */
public class ArticleBaseService {

    @Resource
    public IdWorker idWorker;
    @Resource
    public ArticleMapper articleMapper;
    @Resource
    public ArticleMapperCustom articleMapperCustom;


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
