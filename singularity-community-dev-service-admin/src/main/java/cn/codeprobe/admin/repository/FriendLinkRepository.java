package cn.codeprobe.admin.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cn.codeprobe.pojo.mo.FriendLink;

/**
 * @author Lionido
 */
@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLink, String> {

    /**
     * 门户：获取所有友情链接
     * 
     * @param isDelete 未删除
     * @return List<FriendLinkDO>
     */
    List<FriendLink> getAllByIsDelete(Integer isDelete);

}
