package cn.codeprobe.admin.service.category.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.codeprobe.admin.service.base.AdminBaseService;
import cn.codeprobe.admin.service.category.CategoryWriterService;
import cn.codeprobe.pojo.po.CategoryDO;

/**
 * @author Lionido
 */
@Service
public class CategoryWriterServiceImpl extends AdminBaseService implements CategoryWriterService {

    @Override
    public List<CategoryDO> getCategories() {
        return getCategoryDOList();
    }

}
