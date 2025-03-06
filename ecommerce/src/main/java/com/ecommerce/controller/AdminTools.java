package com.ecommerce.controller;

import com.ecommerce.common.CategoryPageInfo;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Vendor;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.VendorService;
import com.ecommerce.enums.Pagination;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AdminTools {

    private final UserService userService;
    private final VendorService vendorService;
    private final OrderService ordersService;
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminTools(UserService userService, VendorService vendorService,
            OrderService ordersService, ProductService productService,
            CategoryService categoryService) {
        this.userService = userService;
        this.vendorService = vendorService;
        this.ordersService = ordersService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/users/check_login")
    public @ResponseBody
    String checkLoginUnique(@Param("id") Long id, @Param("login") String login) {
        return userService.isLoginUnique(id, login);
    }

    @PostMapping("/user/check")
    public @ResponseBody
    String checkLoginRegistration(@Param("login") String login) {
        return userService.checkLoginRegistration(login) ? "OK" : "Duplicate";
    }

    @PostMapping("/categories/check")
    public @ResponseBody
    String checkCategory(@Param("id") Long id, @Param("title") String title,
            @Param("alias") String alias) {
        return categoryService.checkCategoryTitle(id, title, alias);
    }

    @PostMapping("/vendors/check")
    public @ResponseBody
    String checkVendor(@Param("id") Long id, @Param("title") String title) {
        return vendorService.checkVendorTitle(id, title);
    }

    @GetMapping("/admin/users/page/{pageNum}")
    public String listUsersByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
        Page<User> page = userService.listByPage(pageNum);
        long startCount = (pageNum - 1) * Pagination.USER_PER_PAGE.getValue() + 1;
        long endCount = startCount + Pagination.USER_PER_PAGE.getValue() - 1;
        pageCountMethod(pageNum, model, page, startCount, endCount);
        model.addAttribute("users", page.getContent());
        return "admin/user/users";
    }

    @GetMapping("/admin/vendors/page/{pageNum}")
    public String listVendorsByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
        Page<Vendor> page = vendorService.listByPage(pageNum);
        List<Vendor> vendorList = page.getContent();
        long startCount = (pageNum - 1) * Pagination.VENDORS_PER_PAGE.getValue() + 1;
        long endCount = startCount + Pagination.VENDORS_PER_PAGE.getValue() - 1;
        pageCountMethod(pageNum, model, page, startCount, endCount);
        model.addAttribute("vendors", vendorList);
        return "admin/vendor/vendors";
    }

    @GetMapping("/admin/orders/page/{pageNum}")
    public String listOrdersByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
        Page<Order> page = ordersService.listByPage(pageNum);
        long startCount = (pageNum - 1) * Pagination.ORDERS_PER_PAGE.getValue() + 1;
        long endCount = startCount + Pagination.ORDERS_PER_PAGE.getValue() - 1;
        pageCountMethod(pageNum, model, page, startCount, endCount);
        model.addAttribute("orders", page.getContent());
        return "admin/orders/orders";
    }

    @GetMapping("/admin/categories/page/{pageNum}")
    public String listCategoriesByPage(@PathVariable(name = "pageNum") int pageNum, Model model) {
        CategoryPageInfo pageInfo = new CategoryPageInfo();
        long startCount = (pageNum - 1) * Pagination.TOP_CATEGORIES_PER_PAGE.getValue() + 1;
        long endCount = startCount + Pagination.TOP_CATEGORIES_PER_PAGE.getValue() - 1;
        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }
        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("categories", categoryService.listByPage(pageInfo, pageNum));
        return "admin/category/categories";
    }

    @GetMapping("/admin/products/page/{pageNum}")
    public String listProductsByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
            @Param("sortField") String sortField, @Param("sortDir") String sortDir,
            @Param("keyword") String keyword, @Param("categoryId") Long categoryId) {
        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
        List<Category> listCategories = categoryService.listCategoriesUserInForm();
        List<Product> productList = page.getContent();
        long startCount = (pageNum - 1) * Pagination.PRODUCTS_PER_ADMIN_PAGE.getValue() + 1;
        long endCount = startCount + Pagination.PRODUCTS_PER_ADMIN_PAGE.getValue() - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        if (categoryId != null) {
            model.addAttribute("categoryId", categoryId);
        }
        pageCountMethod(pageNum, model, page, startCount, endCount);
        model.addAttribute("products", productList);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listCategories", listCategories);
        return "admin/product/products";
    }

    public void pageCountMethod(@PathVariable("pageNum") int pageNum, Model model, 
            Page<?> page, long startCount, long endCount) {
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
    }
}
