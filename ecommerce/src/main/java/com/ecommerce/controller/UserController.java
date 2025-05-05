package com.ecommerce.controller;

import com.ecommerce.entity.User;
import com.ecommerce.service.UserService;
import java.security.Principal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.webjars.NotFoundException;

@Controller
@RequestMapping("/profile")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping({"/", ""})
    public String getUserInfo(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("userDetails", userService.getUserByEmail(principal.getName()).getUserInfo());
            model.addAttribute("user", userService.getUserByEmail(principal.getName()));
            return "user/user-main";
        } else {
            model.addAttribute("error", new NotFoundException("User was not found"));
            return "error/page404";
        }
    }

    @GetMapping("/edit")
    public String showEditPage(Principal principal, Model model) {
        model.addAttribute("userDetails", userService.getUserByEmail(principal.getName()).getUserInfo());
        model.addAttribute("user", userService.getUserByEmail(principal.getName()));
        return "user/user-edit";
    }

    @PostMapping("/edit")
    public String editUser(Principal principal, User user, BindingResult bindingResult) {
        User newUser = userService.getUserByEmail(principal.getName());
        if (!user.getPassword().equals("")) {
            if (!passwordEncoder.matches(user.getPassword(), newUser.getPassword())) {
                newUser.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                ObjectError error = new ObjectError("globalError", "The password must not be the same.");
                bindingResult.addError(error);
                System.err.println(bindingResult.hasErrors());
                return "user/user-edit";
            }
        }
        newUser.getUserInfo().setName(user.getUserInfo().getName());
        newUser.getUserInfo().setSurname(user.getUserInfo().getSurname());
        newUser.getUserInfo().setPhone(user.getUserInfo().getPhone());
        userService.saveUser(newUser);
        return "redirect:/profile";
    }
}
