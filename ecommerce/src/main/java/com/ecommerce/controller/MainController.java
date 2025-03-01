package com.ecommerce.controller;

import com.ecommerce.dto.request.UserRegistrationRequest;
import com.ecommerce.dto.request.UserInfoRegistrationRequest;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public MainController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("listCategories", categoryService.findAllEnabled());
        try {
            model.addAttribute("listProducts", productService.getRandomAmountOfProducts());
        } catch (ProductNotFoundException ex) {
            model.addAttribute("error", (ex.getCause() != null) ? ex.getCause().getMessage() : ex.getMessage());
            return "error/page404";
        }
        return "index";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserRegistrationRequest());
        model.addAttribute("userInfo", new UserInfoRegistrationRequest());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(User user, UserInfo userInfo) {
        user.setRole(Role.USER);
        user.setUserInfo(userInfo);
        userInfo.setUser(user);
        userService.saveUser(user);
        return "redirect:/";
    }
}
