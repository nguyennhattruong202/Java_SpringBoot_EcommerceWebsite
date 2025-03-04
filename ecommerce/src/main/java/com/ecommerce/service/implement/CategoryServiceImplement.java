package com.ecommerce.service.implement;

import com.ecommerce.entity.Category;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
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

    @Override
    public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByAliasEnable(alias);
        if (category == null) {
            throw new CategoryNotFoundException("Could not find any category with alias " + alias);
        }
        return category;
    }

    @Override
    public List<Category> getCategoryParents(Category child) {
        List<Category> listParents = new ArrayList<>();
        Category parent = child.getParent();
        while (parent != null) {
            listParents.add(0, parent);
            parent = parent.getParent();
        }
        listParents.add(child);
        return listParents;
    }
}
