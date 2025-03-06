package com.ecommerce.service;

import com.ecommerce.common.CategoryPageInfo;
import com.ecommerce.entity.Category;
import com.ecommerce.exception.CategoryNotFoundException;
import java.util.List;

public interface CategoryService {

    public List<Category> findAllEnabled();

    public Category getCategoryByAlias(String alias) throws CategoryNotFoundException;

    public List<Category> getCategoryParents(Category child);

    public String checkCategoryTitle(Long id, String title, String alias);

    public List<Category> listCategoriesUserInForm();

    public List<Category> listByPage(CategoryPageInfo pageInfo, int pageNum);

    public String convertCyrillic(String message);

    public Category getCategory(Long id) throws CategoryNotFoundException;

    public Category saveCategory(Category category);
    
    public void deleteCategory(Long id) throws CategoryNotFoundException;
}
