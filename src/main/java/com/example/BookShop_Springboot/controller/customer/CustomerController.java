package com.example.BookShop_Springboot.controller.customer;

import com.example.BookShop_Springboot.service.CustomerService;
import com.example.BookShop_Springboot.dto.CustomerDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final String loginPath = "redirect:/login";
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        if (principal == null) {
            return loginPath;
        }
        CustomerDto customer = customerService.getCustomer(principal.getName());
        model.addAttribute("customer", customer);
        model.addAttribute("title", "Profile");
        return "/shop/profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@Valid @ModelAttribute("customer") CustomerDto customerDto,
            @RequestParam(name = "img_avt", required = false) MultipartFile img_avt,
            BindingResult result,
            RedirectAttributes attributes,
            Model model,
            Principal principal) {
        if (principal == null) {
            return loginPath;
        } else {
            if (img_avt != null && !img_avt.isEmpty()) {
                customerService.update(customerDto, img_avt);
            } else {
                customerService.update(customerDto);
            }
            CustomerDto customerUpdate = customerService.getCustomer(principal.getName());
            attributes.addFlashAttribute("success", "Update successfully!");
            model.addAttribute("customer", customerUpdate);
            return "redirect:/profile";
        }
    }

    @PostMapping("/change-password")
    @ResponseBody
    public String changePass(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("repeatNewPassword") String repeatPassword,
            RedirectAttributes attributes,
            Principal principal) {
        if (principal == null) {
            return loginPath;
        } else {
            CustomerDto customer = customerService.getCustomer(principal.getName());
            if (passwordEncoder.matches(oldPassword, customer.getPassword())
                    && !passwordEncoder.matches(newPassword, oldPassword)
                    && !passwordEncoder.matches(newPassword, customer.getPassword())
                    && repeatPassword.equals(newPassword) && newPassword.length() >= 5) {
                customer.setPassword(passwordEncoder.encode(newPassword));
                customerService.changePass(customer);                
                return "success";
            } else {                
                return "failure";
            }
        }
    }
}
