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
     *
     * @param categoryId id
     */
    void delete(Integer categoryId);

    /**
     * 查询文章分类是否存在
     * @param categoryName 分类名称
     * @return true or false
     */
    Boolean checkCategoryIsExist(String categoryName);
}
