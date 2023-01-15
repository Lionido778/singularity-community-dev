package cn.codeprobe.admin.service;

import cn.codeprobe.pojo.Category;
import cn.codeprobe.pojo.bo.CategoryBO;

import java.util.List;

/**
 * @author Lionido
 */

public interface CategoryService {

    /**
     * 保存或更新 Category
     *
     * @param categoryBO 友情链接
     */
    void saveOrUpdateCategory(CategoryBO categoryBO);

    /**
     * 获取 category List
     *
     * @return list
     */
    List<Category> getCategories();

    /**
     * 删除 分类
     * @param categoryId id
     */
    void delete(Integer categoryId);
}
