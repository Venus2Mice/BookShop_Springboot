package com.example.BookShop_Springboot.controller.admin;

import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class OrderController {
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String redirectOrdersPath = "redirect:/admin/orders";
    private final String ordersPath = "/admin/orders";

    private final OrderService orderService;

    @GetMapping("/orders")
    public String getAll(Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        } else {
            List<Order> orderList = orderService.findALlOrders();
            model.addAttribute("orders", orderList);
            return ordersPath;
        }
    }


    @RequestMapping(value = "/accept-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String acceptOrder(Long id, RedirectAttributes attributes, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        } else {
            orderService.acceptOrder(id);
            attributes.addFlashAttribute("success", "Order Accepted");
            return redirectOrdersPath;
        }
    }

    @RequestMapping(value = "/cancel-order", method = {RequestMethod.PUT, RequestMethod.GET})
    public String cancelOrder(Long id, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        } else {
            orderService.cancelOrder(id);
            return redirectOrdersPath;
        }
    }


}
