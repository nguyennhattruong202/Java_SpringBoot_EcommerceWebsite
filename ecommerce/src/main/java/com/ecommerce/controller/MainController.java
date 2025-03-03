package com.ecommerce.controller;

import com.ecommerce.dto.request.UserRegistrationRequest;
import com.ecommerce.dto.request.UserInfoRegistrationRequest;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserInfo;
import com.ecommerce.enums.Role;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.mapper.UserInfoMapper;
import com.ecommerce.mapper.UserMapper;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.webjars.NotFoundException;

@Controller
public class MainController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserService userService;

    public MainController(CategoryService categoryService,
            ProductService productService, UserService userService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
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
    public String registration(UserRegistrationRequest userRegistrationRequest,
            UserInfoRegistrationRequest userInfoRegistrationRequest) {
        userRegistrationRequest.setRole(Role.USER);
        User user = UserMapper.toUser(userRegistrationRequest);
        UserInfo userInfo = UserInfoMapper.toUserInfo(userInfoRegistrationRequest);
        user.setUserInfo(userInfo);
        userInfo.setUser(user);
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/basket")
    public String showShoppingCart(Model model, Principal principal) {
        if (principal != null) {
            List<OrderBasket> orderBasket = userService.getUserByLogin(principal.getName()).getOrderBaskets();
            model.addAttribute("orderBaskets", orderBasket);
            model.addAttribute("order", new Order());
        } else {
            model.addAttribute("error", new NotFoundException("Order basket was not found"));
            return "error/page404";
        }
        return "shopping-cart";
    }
}
