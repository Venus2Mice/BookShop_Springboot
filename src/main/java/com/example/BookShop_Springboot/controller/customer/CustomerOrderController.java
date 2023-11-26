package com.example.BookShop_Springboot.controller.customer;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import com.example.BookShop_Springboot.dto.CustomerDto;
import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.service.CustomerService;
import com.example.BookShop_Springboot.service.OrderService;
import com.example.BookShop_Springboot.service.ShoppingCartService;

@Controller
@RequiredArgsConstructor
public class CustomerOrderController {
    private final String loginPath = "redirect:/login";
    private final CustomerService customerService;
    private final OrderService orderService;

    private final ShoppingCartService cartService;

    @GetMapping("/order")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return loginPath;
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());
            if (customer.getAddress() == null || customer.getEmail() == null || customer.getPhone() == null) {
                model.addAttribute("information", "You need update your information before check out");
                model.addAttribute("customer", customer);
                model.addAttribute("title", "Profile");
                model.addAttribute("page", "Profile");
                return "/shop/profile";
            } else {
                ShoppingCart cart = customerService.findByUsername(principal.getName()).getCart();
                model.addAttribute("customer", customer);
                model.addAttribute("title", "Check-Out");
                model.addAttribute("page", "Check-Out");
                model.addAttribute("shoppingCart", cart);
                model.addAttribute("grandTotal", cart.getTotalItems());
                return "/shop/order";
            }
        }
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        if (principal == null) {
            return loginPath;
        } else {
            Customer customer = customerService.findByUsername(principal.getName());
            List<Order> orderList = customer.getOrders();
            model.addAttribute("orders", orderList);
            model.addAttribute("title", "Order");
            model.addAttribute("page", "Order");
            return "order";
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, RedirectAttributes attributes) {
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/customer/orders";
    }


    @RequestMapping(value = "/add-order", method = {RequestMethod.POST})
    public String createOrder(Principal principal,
                              Model model,
                              HttpSession session) {
        if (principal == null) {
            return loginPath;
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            ShoppingCart cart = customer.getCart();
            Order order = orderService.save(cart);
            session.removeAttribute("totalItems");
            model.addAttribute("order", order);
            model.addAttribute("title", "Order Detail");
            model.addAttribute("page", "Order Detail");
            model.addAttribute("success", "Add order successfully");
            return "redirect:/cart";
        }
    }
}
