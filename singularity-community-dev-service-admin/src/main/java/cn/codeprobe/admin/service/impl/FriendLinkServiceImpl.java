package cn.codeprobe.admin.service.impl;

import cn.codeprobe.admin.service.FriendLinkService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.pojo.mo.FriendLinkMO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lionido
 */
@Service
public class FriendLinkServiceImpl extends AdminBaseService implements FriendLinkService {

    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkRepository.save(friendLinkMO);
    }

    @Override
    public List<FriendLinkMO> getFriendLinks() {
        return friendLinkRepository.findAll();
    }

    @Override
    public void delete(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }
}
