package cn.codeprobe.user.mapper;

import org.springframework.stereotype.Repository;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.po.User;

/**
 * @author Lionido
 */
@Repository
public interface UserMapper extends MyMapper<User> {}