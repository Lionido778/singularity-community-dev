package cn.codeprobe.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.page.PageMethod;

import cn.codeprobe.admin.service.AdminService;
import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;
import cn.codeprobe.pojo.bo.NewAdminBO;
import cn.codeprobe.pojo.po.Admin;
import cn.codeprobe.result.page.PagedGridResult;
import cn.hutool.core.text.CharSequenceUtil;
import tk.mybatis.mapper.entity.Example;

/**
 * @author Lionido
 */
@Service
public class AdminServiceImpl extends AdminBaseService implements AdminService {
    @Override
    public Boolean checkAdminIsExist(String username) {
        Admin admin = queryAdminByUsername(username);
        return admin != null;
    }

    @Override
    public void saveAdminUser(NewAdminBO newAdminBO) {
        Admin admin = new Admin();
        admin.setId(idWorker.nextIdStr());
        admin.setUsername(newAdminBO.getUsername());
        admin.setAdminName(newAdminBO.getAdminName());
        // 密码加密
        String encryptPassword = encryptPassword(newAdminBO.getPassword());
        admin.setPassword(encryptPassword);
        String faceId = newAdminBO.getFaceId();
        // 如果上传了人脸识别则会有 faceId
        if (CharSequenceUtil.isNotBlank(faceId)) {
            admin.setFaceId(faceId);
        }
        admin.setCreatedTime(new Date());
        admin.setUpdatedTime(new Date());

        int result = adminMapper.insertSelective(admin);
        if (result != 1) {
            // 管理员添加失败
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public PagedGridResult pageListAdminUsers(int page, int pageSize) {
        Example example = new Example(Admin.class);
        example.orderBy("createdTime").desc();

        PageMethod.startPage(page, pageSize);
        List<Admin> adminList = adminMapper.selectByExample(example);
        if (adminList.isEmpty()) {
            GlobalExceptionManage.internal(ResponseStatusEnum.ADMIN_QUERY_ERROR);
        }
        return setterPageGrid(adminList, page);
    }
}
