package cn.codeprobe.user.service.impl;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.enums.UserStatus;
import cn.codeprobe.exception.GlobalExceptionManage;
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

    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        // 校验
        AppUser appUser = appUserMapper.selectByPrimaryKey(userId);
        if (appUser == null) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(doStatus);
        // 更新用户状态（冻结或解冻）
        int result = appUserMapper.updateByPrimaryKeySelective(user);
        if (result != 1) {
            GlobalExceptionManage.internal(ResponseStatusEnum.USER_OPERATION_ERROR);
        }
        // 如果冻结，删除该用户会话
        if (doStatus.equals(UserStatus.FROZEN.type)) {
            redisUtil.del(REDIS_USER_TOKEN + ":" + userId);
        }
        // 删除 user info 缓存信息
        redisUtil.del(REDIS_USER_INFO + ":" + userId);
    }
}
