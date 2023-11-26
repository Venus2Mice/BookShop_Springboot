package com.example.BookShop_Springboot.controller.customer;

import java.security.Principal;

import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.service.CategoryService;
import com.example.BookShop_Springboot.service.CustomerService;
import com.example.BookShop_Springboot.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CustomerService customerService;

    private final CategoryService categoryService;

    private final ProductService productService;

    @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
    public String home(Model model, Principal principal, HttpSession session) {
        model.addAttribute("title", "Home");
        model.addAttribute("page", "Home");
        model.addAttribute("categories", categoryService.findAllByActivatedTrue());
        model.addAttribute("products", productService.getAllProductsForCustomer(0));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (principal != null && authentication.getAuthorities().contains(new SimpleGrantedAuthority("CUSTOMER"))) {
            Customer customer = customerService.findByEmail(principal.getName());
            // session.setAttribute("username", customer.getFirstName() + " " +
            // customer.getLastName());
            session.setAttribute("username", customer.getUsername());
            session.setAttribute("img", customer.getImg_avt());
            ShoppingCart shoppingCart = customer.getCart();
            if (shoppingCart != null) {
                session.setAttribute("totalItems", shoppingCart.getTotalItems());
            }
        }
        return "/shop/mainpage";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("title", "Contact");
        model.addAttribute("page", "Contact");
        return "/shop/contact-us";
    }
}
