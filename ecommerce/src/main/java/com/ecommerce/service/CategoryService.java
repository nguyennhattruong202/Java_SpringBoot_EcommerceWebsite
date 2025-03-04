package com.ecommerce.service;

import com.ecommerce.entity.Category;
import com.ecommerce.exception.CategoryNotFoundException;
import java.util.List;

public interface CategoryService {

    public List<Category> findAllEnabled();

    public Category getCategoryByAlias(String alias) throws CategoryNotFoundException;

    public List<Category> getCategoryParents(Category child);
}
