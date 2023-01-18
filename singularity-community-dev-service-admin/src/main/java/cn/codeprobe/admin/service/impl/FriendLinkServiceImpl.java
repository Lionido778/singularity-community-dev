package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.FriendLinkService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.FriendLinkBO;
import cn.codeprobe.pojo.mo.FriendLinkDO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Lionido
 */
@Service
public class FriendLinkServiceImpl extends AdminBaseService implements FriendLinkService {

    @Override
    public void saveOrUpdateFriendLink(FriendLinkBO friendLinkBO) {
        // 检查友情链接是否存在
        String linkName = friendLinkBO.getLinkName();
        Boolean isExist = checkFriendLinkIsExist(linkName);
        if (Boolean.TRUE.equals(isExist)){
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_FRIEND_LINK_IS_EXISTED);
        }
        FriendLinkDO friendLinkDO = new FriendLinkDO();
        BeanUtils.copyProperties(friendLinkBO, friendLinkDO);
        // 保存或更新
        friendLinkRepository.save(friendLinkDO);
    }

    @Override
    public List<FriendLinkDO> listFriendLinks() {
        return friendLinkRepository.findAll();
    }

    @Override
    public void removeFriendLink(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }

    @Override
    public Boolean checkFriendLinkIsExist(String linkName) {
        FriendLinkDO friendLinkDO = new FriendLinkDO();
        friendLinkDO.setLinkName(linkName);
        Example<FriendLinkDO> example = Example.of(friendLinkDO);
        Optional<FriendLinkDO> one = friendLinkRepository.findOne(example);
        return one.isPresent();
    }

}
