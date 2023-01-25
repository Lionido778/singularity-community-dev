package cn.codeprobe.admin.service.category;

import java.util.List;

import cn.codeprobe.pojo.po.CategoryDO;

/**
 * 门户：CategoryService
 * 
 * @author Lionido
 */

public interface CategoryPortalService {

    /**
     * 门户 获取 category List
     *
     * @return list
     */
    List<CategoryDO> getCategories();

}
