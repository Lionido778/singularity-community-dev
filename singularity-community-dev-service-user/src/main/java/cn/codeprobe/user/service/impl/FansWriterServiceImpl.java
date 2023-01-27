package cn.codeprobe.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.pojo.po.FansDO;
import cn.codeprobe.pojo.vo.FansRegionRatioVO;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.service.FansWriterService;
import cn.codeprobe.user.service.base.UserBaseService;

/**
 * @author Lionido
 */
@Service
public class FansWriterServiceImpl extends UserBaseService implements FansWriterService {

    @Override
    public PagedGridResult pageListFans(String writerId, Integer page, Integer pageSize) {
        FansDO fansDO = new FansDO();
        fansDO.setWriterId(writerId);
        // 查询粉丝(宽表)分页列表
        PageHelper.startPage(page, pageSize);
        List<FansDO> list = fansMapper.select(fansDO);
        return setterPageGrid(list, page);
    }

    @Override
    public Long countFansBySex(String writerId, Integer sex) {
        FansDO fansDO = new FansDO();
        fansDO.setWriterId(writerId);
        fansDO.setSex(sex);
        int count = fansMapper.selectCount(fansDO);
        return Long.parseLong(String.valueOf(count));
    }

    @Override
    public List<FansRegionRatioVO> countFansByRegion(String writerId) {
        ArrayList<FansRegionRatioVO> list = new ArrayList<>();
        for (String region : REGIONS) {
            FansDO fansDO = new FansDO();
            fansDO.setWriterId(writerId);
            fansDO.setProvince(region);
            int count = fansMapper.selectCount(fansDO);
            FansRegionRatioVO fansRegionRatioVO = new FansRegionRatioVO();
            fansRegionRatioVO.setName(region);
            fansRegionRatioVO.setValue(Long.parseLong(String.valueOf(count)));
            list.add(fansRegionRatioVO);
        }
        return list;
    }
}
