package cn.codeprobe.admin.service.category;

import java.util.List;

import cn.codeprobe.pojo.po.Category;

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
    List<Category> getCategories();

}
