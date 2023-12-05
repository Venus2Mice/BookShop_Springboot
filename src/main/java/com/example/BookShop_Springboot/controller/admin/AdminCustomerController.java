package com.example.BookShop_Springboot.controller.admin;

import java.util.List;
import java.util.Optional;

import com.example.BookShop_Springboot.model.Customer;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.example.BookShop_Springboot.service.CustomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminCustomerController {
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String redirectCustomerPath = "redirect:/admin/customer";
    private final String customerPath = "/admin/customer";

    private final CustomerService customerService;

    @GetMapping("/customer")
    public String customer(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return redirectLoginPath;
        }
        model.addAttribute("title", "Manage Customer");
        List<Customer> customers = customerService.findALl();
        model.addAttribute("customer", customers);
        model.addAttribute("size", customers.size());
        model.addAttribute("customerNew", new Customer());
        return customerPath;
    }

    @RequestMapping(value = "/disable-customer", method = { RequestMethod.GET, RequestMethod.PUT })
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.disableById(id);
            redirectAttributes.addFlashAttribute("success", "Disable successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of customer, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return redirectCustomerPath;
    }

    @RequestMapping(value = "/enable-customer", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enable(Long id, RedirectAttributes redirectAttributes) {
        try {
            customerService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enable successfully");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of customer, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return redirectCustomerPath;
    }

}
