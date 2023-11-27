package com.example.BookShop_Springboot.controller.customer;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.model.Order;
import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.service.CustomerService;
import com.example.BookShop_Springboot.service.OrderService;

@Controller
@RequiredArgsConstructor
public class CustomerOrderController {
    private final String loginPath = "redirect:/login";
    private final CustomerService customerService;
    private final OrderService orderService;

    @GetMapping("/order")
    public String checkOut(Principal principal, Model model) {
        if (principal == null) {
            return loginPath;
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            List<Order> orderList = orderService.findAll(principal.getName());
            if (orderList.size() == 0) {
                model.addAttribute("blank", "Bạn chưa đặt đơn hàng nào !");
                model.addAttribute("title", "Lịch sử mua hàng");
                model.addAttribute("size", orderList.size());
                return "/shop/order";
            }
            model.addAttribute("customer", customer);
            model.addAttribute("title", "Lịch sử mua hàng");
            model.addAttribute("orders", orderList);
            model.addAttribute("size", orderList.size());
            return "/shop/order";
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

    @RequestMapping(value = "/cancel-order", method = { RequestMethod.PUT, RequestMethod.GET })
    public String cancelOrder(Long id, RedirectAttributes attributes) {
        orderService.cancelOrder(id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/customer/orders";
    }

    @RequestMapping(value = "/cancel-order-by-customer/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String cancelOrderByCus(@PathVariable("id") Long id, RedirectAttributes attributes) {
        orderService.cancelOrderByCustomer(id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/order";
    }

    @RequestMapping(value = "/add-order", method = { RequestMethod.POST })
    public String createOrder(Principal principal,
            Model model,
            HttpSession session) {
        if (principal == null) {
            return loginPath;
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            if (customer.getAddress() == null || customer.getEmail() == null || customer.getPhone() == null) {
                return "redirect:/profile?edit";
            } else {
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
}
