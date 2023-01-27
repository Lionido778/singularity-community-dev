package cn.codeprobe.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.codeprobe.pojo.po.Fans;
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
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        // 查询粉丝(宽表)分页列表
        PageHelper.startPage(page, pageSize);
        List<Fans> list = fansMapper.select(fans);
        return setterPageGrid(list, page);
    }

    @Override
    public Long countFansBySex(String writerId, Integer sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex);
        int count = fansMapper.selectCount(fans);
        return Long.parseLong(String.valueOf(count));
    }

    @Override
    public List<FansRegionRatioVO> countFansByRegion(String writerId) {
        ArrayList<FansRegionRatioVO> list = new ArrayList<>();
        for (String region : REGIONS) {
            Fans fans = new Fans();
            fans.setWriterId(writerId);
            fans.setProvince(region);
            int count = fansMapper.selectCount(fans);
            FansRegionRatioVO fansRegionRatioVO = new FansRegionRatioVO();
            fansRegionRatioVO.setName(region);
            fansRegionRatioVO.setValue(Long.parseLong(String.valueOf(count)));
            list.add(fansRegionRatioVO);
        }
        return list;
    }
}
