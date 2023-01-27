package cn.codeprobe.user.service;

import java.util.List;

import cn.codeprobe.pojo.vo.FansRegionRatioVO;
import cn.codeprobe.result.page.PagedGridResult;

/**
 * @author Lionido
 */
public interface FansWriterService {

    /**
     * 查询作家的所有粉丝
     *
     * @param writerId 作者ID
     * @param page 当前页
     * @param pageSize 每页查询数量
     * @return PagedGridResult
     */
    PagedGridResult pageListFans(String writerId, Integer page, Integer pageSize);

    /**
     * 根据性别查询作家粉丝数量
     * 
     * @param writerId 作家ID
     * @param sex 性别
     * @return count
     */
    Long countFansBySex(String writerId, Integer sex);

    /**
     * 根据地域查询作家粉丝数量
     *
     * @param writerId 作家ID
     * @return List<FansRegionRatioVO>
     */
    List<FansRegionRatioVO> countFansByRegion(String writerId);
}
