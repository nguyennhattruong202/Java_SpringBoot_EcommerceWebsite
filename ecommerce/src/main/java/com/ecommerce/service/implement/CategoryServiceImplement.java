package com.ecommerce.service.implement;

import com.ecommerce.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CategoryServiceImplement implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImplement(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllEnabled() {
        return categoryRepository.findAllEnabled();
    }
}
