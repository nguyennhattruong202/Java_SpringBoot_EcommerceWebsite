package com.ecommerce.controller;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.enums.Pagination;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final AdminTools adminTools;

    public ProductController(CategoryService categoryService, ProductService productService, AdminTools adminTools) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.adminTools = adminTools;
    }

    @GetMapping("/demo")
    public String viewDemo(Model model) {
        return viewDemoByPage(1, model);
    }

    @GetMapping("/demo/page/{pageNumber}")
    public String viewDemoByPage(@PathVariable("pageNumber") int pageNumber, Model model) {
        model.addAttribute("pageNumber", pageNumber); 
        return "demo";
    }

    @GetMapping({"/category/{category_alias}"})
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
        return viewCategoryByPage(alias, model, 1);
    }

    @GetMapping("/category/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, Model model,
            @PathVariable("pageNum") int pageNum) {
        try {
            Category category = categoryService.getCategoryByAlias(alias);
            Page<Product> pageProduct = productService.listByCategory(pageNum, category.getId());
            long startCount = (pageNum - 1) * Pagination.PRODUCT_PER_PAGE.getValue() + 1;
            long endCount = startCount + Pagination.PRODUCT_PER_PAGE.getValue() - 1;
            adminTools.pageCountMethod(pageNum, model, pageProduct, startCount, endCount);
            model.addAttribute("pageTitle", category.getTitle());
            model.addAttribute("listCategoryParents", categoryService.getCategoryParents(category));
            model.addAttribute("listProducts", pageProduct.getContent());
            model.addAttribute("category", category);
            return "product/productsByCategory";
        } catch (CategoryNotFoundException ex) {
            model.addAttribute("error", ex.getLocalizedMessage());
            return "error/page404";
        }
    }

    @GetMapping("/product/{product_alias}")
    public String viewProductDetails(@PathVariable("product_alias") String alias, Model model) {
        try {
            Product product = productService.getProduct(alias);
            model.addAttribute("listCategoryParents", categoryService.getCategoryParents(product.getCategory()));
            model.addAttribute("product", product);
            return "product/productPage";
        } catch (ProductNotFoundException e) {
            model.addAttribute("error", e.getLocalizedMessage());
            return "error/page404";
        }
    }

    @PostMapping("/products/check_unique")
    public @ResponseBody
    String checkUnique(@Param("id") Long id, @Param("title") String title) {
        return productService.checkUnique(id, title);
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model) {
        return searchByPage(keyword, 1, model);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword, @PathVariable("pageNum") int pageNum, Model model) {
        Page<Product> productsPage = productService.search(keyword, pageNum);
        long startCount = (pageNum - 1) * Pagination.SEARCH_RESULTS_PAGE.getValue() + 1;
        long endCount = startCount + Pagination.SEARCH_RESULTS_PAGE.getValue() - 1;
        adminTools.pageCountMethod(pageNum, model, productsPage, startCount, endCount);
        model.addAttribute("pageTitle", StringUtils.capitalize(keyword) + " - Search Result");
        model.addAttribute("keyword", keyword);
        model.addAttribute("resultList", productsPage.getContent());
        return "product/search_result";
    }
}
