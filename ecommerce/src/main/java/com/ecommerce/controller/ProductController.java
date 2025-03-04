package com.ecommerce.controller;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {

    public static final int PRODUCT_PER_PAGE = 10;

    private final CategoryService categoryService;
    private final ProductService productService;
    private final AdminTools adminTools;

    public ProductController(CategoryService categoryService, ProductService productService, AdminTools adminTools) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.adminTools = adminTools;
    }

    @GetMapping("/category/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model, @PathVariable("pageNum") int pageNum) {
        try {
            Category category = categoryService.getCategoryByAlias(alias);
            Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());
            long startCount = (pageNum - 1) * PRODUCT_PER_PAGE + 1;
            long endCount = startCount + PRODUCT_PER_PAGE - 1;
            adminTools.pageCountMethod(pageNum, model, pageProduct, startCount, endCount);
            model.addAttribute("pageTitle", category.getTitle());
            model.addAttribute("listCategoryParents", categoryService.getCategoryParents(category));
            model.addAttribute("listProducts", pageProduct.getContent());
            model.addAttribute("category", category);
            return "product/product_by_category";
        } catch (CategoryNotFoundException ex) {
            model.addAttribute("error", ex.getLocalizedMessage());
            return "error/page404";
        }
    }
}
