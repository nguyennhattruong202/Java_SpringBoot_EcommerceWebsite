package com.ecommerce.controller;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderType;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.NotFoundException;

@Controller
public class OrderController {

    private final UserService userService;
    private final OrderService orderService;
    private final JavaMailSender javaMailSender;

    public OrderController(UserService userService, OrderService orderService, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.orderService = orderService;
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/orders")
    public String showOrder(Model model, Principal principal) {
        if (principal != null) {
            User user = userService.getUserByEmail(principal.getName());
            model.addAttribute("orders", orderService.getAllOrdersByUser(user));
            return "user/orders";
        } else {
            model.addAttribute("error", new NotFoundException("Orders was not found"));
            return "error/page404";
        }
    }

    @GetMapping("/payment")
    public String createOrders(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("order", new Order());
            model.addAttribute("user", userService.getUserByEmail(principal.getName()));
            model.addAttribute("orderBaskets", userService.getUserByEmail(principal.getName()).getOrderBaskets());
            model.addAttribute("waiting", OrderType.PENDING);
            model.addAttribute("payed", OrderType.PAID);
            return "checkout";
        } else {
            model.addAttribute("error", new NotFoundException("Orders for payment was not found"));
            return "error/page404";
        }
    }

    @PostMapping("/payment")
    public String saveOrder(Order newOrder, Principal principal, Model model, RedirectAttributes redirectAttribute) {
        User user = userService.getUserByEmail(principal.getName());
        List<OrderBasket> orderBaskets = user.getOrderBaskets();
        newOrder.setUser(user);
        newOrder.setTotalPrice(orderService.countSum(orderBaskets));
        try {
            orderService.saveOrder(newOrder);
            redirectAttribute.addFlashAttribute("message", "Order was completed! Check your email!");
            sendVerificationEmail(newOrder);
        } catch (JpaSystemException | MessagingException | UnsupportedEncodingException ex) {
            model.addAttribute("error", ex.getCause().getCause().getMessage());
            return "error/page404";
        }
        return "redirect:/orders";
    }

    private void sendVerificationEmail(Order order) throws MessagingException, UnsupportedEncodingException {
        String shipping = order.getShippingType() == 0 ? "Ukr poshta" : "Nova poshta";
        String subject = "Thank you ordering in SENKO";
        String senderName = "Senko store";
        String mailContent = "<p><b>Order number: </b>" + order.getId() + "</p>";
        mailContent += "<p><b>Payment: </b>" + order.getOrderStatus() + "</p>";
        mailContent += "<p><b>Shipping: </b>" + shipping + "</p>";
        mailContent += "<p><b>City: </b>" + order.getCity() + "</p>";
        mailContent += "<p><b>Address: </b>" + order.getAddress() + "</p>";
        mailContent += "<p><b>Order total: </b>" + order.getTotalPrice() + "</p>";
        mailContent += "<hr/><img src='cid:logoImage' width=150/>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("senkoShop@outlook.com", senderName);
        helper.setTo(order.getUser().getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        ClassPathResource classPathResource = new ClassPathResource("/static/assets/email.gif");
        helper.addInline("logoImage", classPathResource);
        javaMailSender.send(message);
    }
}
