package com.ecommerce.controller;

import com.ecommerce.entity.Category;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserInfo;
import com.ecommerce.entity.Vendor;
import com.ecommerce.enums.OrderType;
import com.ecommerce.enums.Role;
import com.ecommerce.exception.CategoryNotFoundException;
import com.ecommerce.exception.OrderNotFoundException;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.exception.UserNotFoundException;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.OrderBasketService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserInfoService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.VendorService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminTools adminTools;
    private final ProductService productService;
    private final VendorService vendorService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final UserInfoService userInfoService;
    private final OrderService orderService;
    private final OrderBasketService orderBasketService;

    public AdminController(AdminTools adminTools, ProductService productService,
            VendorService vendorService, CategoryService categoryService,
            UserService userService, UserInfoService userInfoService,
            OrderService orderService, OrderBasketService orderBasketService) {
        this.adminTools = adminTools;
        this.productService = productService;
        this.vendorService = vendorService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.userInfoService = userInfoService;
        this.orderService = orderService;
        this.orderBasketService = orderBasketService;
    }

    @GetMapping({"", "/", "/admin-panel"})
    public String showAdminPanel() {
        return "admin/admin-panel";
    }

    @GetMapping("/products")
    public String listProductsFirstPage(Model model) {
        return adminTools.listProductsByPage(1, model, "title", "asc", null, 0L);
    }

    @GetMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable(name = "id") Long id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("product", productService.getProduct(id));
            model.addAttribute("vendorList", vendorService.getAllVendors());
            model.addAttribute("categoryList", categoryService.listCategoriesUserInForm());
            return "admin/product/product_form";
        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/product/products";
        }
    }

    @GetMapping("/products/new")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("vendorList", vendorService.getAllVendors());
        model.addAttribute("categoryList", categoryService.listCategoriesUserInForm());
        return "admin/product/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirect) {
        productService.saveProduct(product);
        redirect.addFlashAttribute("message", "The product was saved successfully");
        return "redirect:/admin/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Long id, RedirectAttributes redirect) {
        try {
            productService.deleteProduct(id);
            redirect.addFlashAttribute("message", "The product ID " + id + " has been deleted successfully");
        } catch (ProductNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    @GetMapping("/users")
    public String listUsersFirstPage(Model model) {
        return adminTools.listUsersByPage(1, model);
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("userInfo", new UserInfo());
        model.addAttribute("roles", Role.values());
        return "admin/user/user_form";
    }

    @PostMapping("/users/save")
    public String createUser(UserInfo userInfo, User user, RedirectAttributes redirect) {
        user.setUserInfo(userInfo);
        userInfo.setUser(user);
        userService.saveUser(user);
        redirect.addFlashAttribute("message", "The user was saved successfully");
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String updateUser(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("user", userService.getUser(id));
            model.addAttribute("userInfo", userInfoService.getUserDetail(id));
            model.addAttribute("roles", Role.values());
            return "admin/user/user_form";
        } catch (UserNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id, RedirectAttributes redirect) {
        try {
            userService.deleteUser(id);
            redirect.addFlashAttribute("message", "The user ID " + id + " has been deleted successfully");
        } catch (UserNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/categories")
    public String listCategoriesFirstPage(Model model) {
        return adminTools.listCategoriesByPage(1, model);
    }

    @GetMapping("/categories/edit/{id}")
    public String updateCategory(@PathVariable Long id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("category", categoryService.getCategory(id));
            model.addAttribute("categoryList", categoryService.listCategoriesUserInForm());
            return "admin/category/category_form";
        } catch (CategoryNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/categories";
        }
    }

    @GetMapping("/categories/new")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categoryList", categoryService.listCategoriesUserInForm());
        return "admin/category/category_form";
    }

    @PostMapping("/categories/save")
    public String saveCategory(@ModelAttribute Category category, RedirectAttributes attributes) {
        categoryService.saveCategory(category);
        attributes.addFlashAttribute("message", "The category has been saved successfully");
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Long id, RedirectAttributes redirect) {
        try {
            categoryService.deleteCategory(id);
            redirect.addFlashAttribute("message", "The category ID " + id + " has been deleted successfully");
        } catch (CategoryNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/vendors")
    public String listVendorsFirstPage(Model model) {
        return adminTools.listVendorsByPage(1, model);
    }

    @GetMapping("/vendors/new")
    public String newVendor(Model model) {
        model.addAttribute("vendor", new Vendor());
        return "admin/vendor/vendor_form";
    }

    @PostMapping("/vendors/save")
    public String createVendor(Vendor vendor, RedirectAttributes redirect) {
        vendorService.saveVendor(vendor);
        redirect.addFlashAttribute("message", "The vendor was saved successfully");
        return "redirect:/admin/vendors";
    }

    @GetMapping("/vendors/edit/{id}")
    public String updateVendor(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirect) {
        try {
            Vendor vendor = vendorService.getVendor(id);
            model.addAttribute("vendor", vendor);
            return "admin/vendor/vendor_form";
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/vendors";
        }
    }

    @GetMapping("/vendors/delete/{id}")
    public String deleteVendor(@PathVariable(name = "id") Long id, RedirectAttributes redirect) {
        try {
            vendorService.deleteVendor(id);
            redirect.addFlashAttribute("message", "The vendor ID " + id + " has been deleted successfully");
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/vendors";
    }

    @GetMapping("/orders")
    public String listOrdersFirstPage(Model model) {
        return adminTools.listOrdersByPage(1, model);
    }

    @PostMapping("/orders/save")
    public String createOrder(Order order, RedirectAttributes redirect) {
        orderService.saveOrder(order);
        redirect.addFlashAttribute("message", "The order was saved successfully");
        return "redirect:/admin/orders";
    }

    @GetMapping("/orders/edit/{id}")
    public String updateOrder(@PathVariable(name = "id") Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("orderTypes", OrderType.values());
            model.addAttribute("order", orderService.getOrder(id));
            return "admin/orders/order_form";
        } catch (NotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
            return "redirect:/admin/orders";
        }
    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable(name = "id") Long id, RedirectAttributes redirect) {
        try {
            orderService.deleteOrder(id);
            redirect.addFlashAttribute("message", "The orders ID " + id + " has been deleted successfully");
        } catch (OrderNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/order_baskets")
    public String allOrderBasket(Model model) {
        try {
            model.addAttribute("orderBaskets", orderBasketService.getAllOrderBaskets());
            return "admin/order_basket/order_baskets";
        } catch (NotFoundException ex) {
            model.addAttribute("error", ex.getCause().getCause().getMessage());
            return "/error/page404";
        }
    }
}
