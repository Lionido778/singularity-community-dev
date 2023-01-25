package cn.codeprobe.admin.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import cn.codeprobe.pojo.mo.FriendLinkDO;

/**
 * @author Lionido
 */
@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLinkDO, String> {}
