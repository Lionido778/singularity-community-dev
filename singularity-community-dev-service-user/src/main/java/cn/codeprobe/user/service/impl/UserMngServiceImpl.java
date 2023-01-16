package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.pojo.AppUser;
import cn.codeprobe.result.page.PagedGridResult;
import cn.codeprobe.user.service.UserMngService;
import cn.codeprobe.user.service.base.UserBaseService;
import cn.hutool.core.text.CharSequenceUtil;
import com.github.pagehelper.page.PageMethod;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author Lionido
 */
@Service
public class UserMngServiceImpl extends UserBaseService implements UserMngService {
    @Override
    public PagedGridResult getUserList(String nickname, Integer activeStatus, Date startTime, Date endTime, Integer page, Integer pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();
        if (CharSequenceUtil.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        if (UserStatus.isUserStatusValid(activeStatus)) {
            criteria.andEqualTo("activeStatus", activeStatus);
        }
        if (startTime != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startTime);
        }
        if (endTime != null) {
            criteria.andLessThanOrEqualTo("createdTime", endTime);
        }
        PageMethod.startPage(page, pageSize);
        List<AppUser> list = appUserMapper.selectByExample(example);
        return setterPageGrid(list, page);
    }
}
