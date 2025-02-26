package com.ecommerce.service;

import com.ecommerce.entity.Category;
import java.util.List;

public interface CategoryService {

    public List<Category> findAllEnabled();
}
