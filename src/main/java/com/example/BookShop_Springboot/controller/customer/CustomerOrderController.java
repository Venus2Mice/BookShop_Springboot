package com.example.BookShop_Springboot.controller.customer;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.example.BookShop_Springboot.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

@Controller
@RequiredArgsConstructor
public class CustomerOrderController {
    private final String loginPath = "redirect:/login";

    private final CustomerService customerService;
    private final OrderService orderService;
    private final PaypalService paypalService;

    public static final String SUCCESS_URL = "check-out/paypal/success";
    public static final String CANCEL_URL = "check-out/paypal/cancel";

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

    @RequestMapping(value = "/cancel-order-by-customer/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String cancelOrderByCus(@PathVariable("id") String id, RedirectAttributes attributes) {
        orderService.cancelOrderByCustomer(id);
        attributes.addFlashAttribute("success", "Cancel order successfully!");
        return "redirect:/order";
    }

    @RequestMapping(value = "/accept-order-by-customer/{id}", method = { RequestMethod.PUT, RequestMethod.GET })
    public String acceptOrderByCus(@PathVariable("id") String id, RedirectAttributes attributes) {
        orderService.acceptOrderByCustomer(id);
        attributes.addFlashAttribute("success", "Accept order successfully!");
        return "redirect:/order";
    }

    @RequestMapping(value = "/add-order", method = { RequestMethod.POST })
    public String createOrder(Principal principal, @RequestParam(name = "address", required = false) String address,
            @RequestParam(name = "phoneNumber", required = false) String phone,
            @RequestParam("paymentMethod") String paymentMethod,
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
                if (paymentMethod.equals("Paypal")) {
                    try {
                        double exchangeRate = 0.000043;
                        Payment payment = paypalService.createPayment(cart.getTotalPrice() * exchangeRate, "USD",
                                "paypal", "order", address, "http://localhost:8019/" + CANCEL_URL,
                                "http://localhost:8019/" + SUCCESS_URL);
                        for (Links link : payment.getLinks()) {
                            if (link.getRel().equals("approval_url")) {
                                session.setAttribute("phone", phone);
                                session.setAttribute("address", address);
                                return "redirect:" + link.getHref();
                            }
                        }
                    } catch (PayPalRESTException e) {

                        e.printStackTrace();
                    }
                    return "redirect:/orders";
                }
                Order order = orderService.save(cart, address, phone, paymentMethod);
                session.removeAttribute("totalItems");
                model.addAttribute("order", order);
                model.addAttribute("title", "Order Detail");
                model.addAttribute("page", "Order Detail");
                model.addAttribute("success", "Add order successfully");
                return "redirect:/order";
            }
        }
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay(HttpSession session) {
        session.removeAttribute("phone");
        session.removeAttribute("address");
        return "/shop/paypal-cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
            HttpSession session,
            Principal principal) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                Customer customer = customerService.findByEmail(principal.getName());
                ShoppingCart cart = customer.getCart();
                orderService.savePaypal(cart, session.getAttribute("phone").toString(),
                        session.getAttribute("address").toString());
                session.removeAttribute("phone");
                session.removeAttribute("address");
                session.removeAttribute("totalItems");
                return "redirect:/order";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/";
    }
}
