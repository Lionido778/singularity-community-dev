package cn.codeprobe.user.mapper;

import cn.codeprobe.mapper.MyMapper;
import cn.codeprobe.pojo.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}