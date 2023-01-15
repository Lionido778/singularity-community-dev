package cn.codeprobe.admin.repository;

import cn.codeprobe.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Lionido
 */
@Repository
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO, String> {
}
