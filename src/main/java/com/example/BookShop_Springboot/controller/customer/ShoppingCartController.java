package com.example.BookShop_Springboot.controller.customer;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.model.CartItem;
import com.example.BookShop_Springboot.model.Customer;
import com.example.BookShop_Springboot.model.ShoppingCart;
import com.example.BookShop_Springboot.service.CustomerService;
import com.example.BookShop_Springboot.service.ProductService;
import com.example.BookShop_Springboot.service.ShoppingCartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ShoppingCartController {
    private final String loginPath = "redirect:/login";

    private final ShoppingCartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    @GetMapping("/cart")
    public String cart(Model model, Principal principal,HttpSession session) {
        if (principal == null) {
            return loginPath;
        } else {
            Customer customer = customerService.findByEmail(principal.getName());
            ShoppingCart cart = customer.getCart();
            if (cart == null) {
                model.addAttribute("check");

            }
            if (cart != null) {
                List<CartItem> list = cartService.sortCartItems(cart.getCartItems());
                model.addAttribute("grandTotal", cart.getTotalPrice());
                session.setAttribute("totalItems", cart.getTotalItems());
                model.addAttribute("cartItems", list);
            }
            model.addAttribute("shoppingCart", cart);
            model.addAttribute("title", "Cart");
            return "/shop/cart";
        }

    }

    @PostMapping("/add-to-cart")
    @ResponseBody
    public String addItemToCart(@RequestParam("id") Long id,
            @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity,
            HttpServletRequest request,
            Model model,
            Principal principal,
            HttpSession session) {

        ProductDto productDto = productService.getById(id);
        if (principal == null) {
            return "redirect:/login";
        } else {
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.addItemToCart(productDto, quantity, username);
            session.setAttribute("totalItems", shoppingCart.getTotalItems());
            model.addAttribute("shoppingCart", shoppingCart);
        }
        return "redirect:" + request.getHeader("Referer");
    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=update")
    @ResponseBody
    public String updateCart(@RequestParam("id") Long id,
            @RequestParam("quantity") int quantity,
            Model model,
            Principal principal) {
        if (principal == null) {
            return loginPath;
        } else {
            ProductDto productDto = productService.getById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.updateCart(productDto, quantity, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/customer/cart";
        }

    }

    @RequestMapping(value = "/update-cart", method = RequestMethod.POST, params = "action=delete")
    @ResponseBody
    public String deleteItem(@RequestParam("id") Long id,
            Model model,
            Principal principal) {
        if (principal == null) {
            return loginPath;
        } else {
            ProductDto productDto = productService.getById(id);
            String username = principal.getName();
            ShoppingCart shoppingCart = cartService.removeItemFromCart(productDto, username);
            model.addAttribute("shoppingCart", shoppingCart);
            return "redirect:/customer/cart";
        }
    }
}
