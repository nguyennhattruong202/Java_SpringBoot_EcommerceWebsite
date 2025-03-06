package com.ecommerce.controller;

import com.ecommerce.entity.User;
import com.ecommerce.service.OrderBasketService;
import com.ecommerce.service.UserService;
import java.security.Principal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShoppingCartRestController {

    private final OrderBasketService orderBasketService;
    private final UserService userService;

    public ShoppingCartRestController(OrderBasketService orderBasketService, UserService userService) {
        this.orderBasketService = orderBasketService;
        this.userService = userService;
    }

    @PostMapping("/basket/add/{pid}/{qty}")
    public String addProductToBasket(@PathVariable("pid") Long productId, @PathVariable("qty") Integer quantity, Principal principal) {
        if (principal == null) {
            return "You must login to add this product to your shopping basket";
        }
        User user = userService.getUserByLogin(principal.getName());
        if (user == null) {
            return "You must login to add this product to your shopping basket";
        }
        Integer addedQuantity = orderBasketService.addProduct(productId, quantity, user);
        return addedQuantity > 1 ? addedQuantity + " items of this product were added to your shopping basket" : addedQuantity + " item of this product was added to your shopping basket";
    }

    @PostMapping("/basket/update/{pid}/{qty}")
    public String updateQuantity(@PathVariable("pid") Long productId, @PathVariable("qty") Integer quantity, Principal principal) {
        if (principal == null) {
            return "You must login to update quantity";
        }
        User user = userService.getUserByLogin(principal.getName());
        if (user == null) {
            return "You must login to update quantity";
        }
        float subtotal = orderBasketService.updateQuantity(productId, quantity, user);
        return String.valueOf(subtotal);
    }

    @PostMapping("/basket/remove/{pid}")
    public String removeProductFromBasket(@PathVariable("pid") Long productId, Principal principal) {
        if (principal == null) {
            return "You must login to remove product";
        }
        User user = userService.getUserByLogin(principal.getName());
        if (user == null) {
            return "You must login to remove product";
        }
        orderBasketService.removeProduct(productId, user);
        return "The product has been removed from your shopping basket.";
    }
}
