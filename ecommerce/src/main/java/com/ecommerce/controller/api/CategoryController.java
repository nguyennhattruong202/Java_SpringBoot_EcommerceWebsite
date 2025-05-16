package com.ecommerce.controller.api;

import com.ecommerce.entity.Category;
import com.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/categories")
    @Operation(summary = "Get categories list", description = "Return categories list")
    public List<Category> getCategoriesEnable() {
        return categoryService.findAllEnabled();
    }
}
