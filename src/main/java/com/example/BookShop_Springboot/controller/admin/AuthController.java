package com.example.BookShop_Springboot.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.BookShop_Springboot.dto.AdminDto;
import com.example.BookShop_Springboot.model.Admin;
import com.example.BookShop_Springboot.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AuthController {
    private final String loginPath = "/admin/login";
    private final String indexPath = "/admin/index";
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String registerPath = "/admin/register";
    private final String forgotPasswordPath = "/admin/forgot-password";

    @Autowired
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("title", "Login Page");
        return loginPath;
    }

    @RequestMapping("/index")
    public String index(Model model ,HttpSession session) {
        model.addAttribute("title", "Home Page");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("usernameLogin",adminService.findByEmail(authentication.getName()).getUsername());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return redirectLoginPath;
        }
        return indexPath;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return registerPath;
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "Forgot Password");
        return forgotPasswordPath;
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
            BindingResult result,
            Model model) {

        try {
            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                return registerPath;
            }
            String email = adminDto.getEmail();
            Admin admin = adminService.findByEmail(email);
            if (admin != null) {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("emailError", "Your email has been registered!");
                return registerPath;
            }
            if (adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("success", "Register successfully!");
            } else {
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError", "Your password maybe wrong! Check again!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errors", "The server has been wrong!");
        }
        return registerPath;
    }
}
