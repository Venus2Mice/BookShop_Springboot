package com.example.BookShop_Springboot.controller.admin;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BookShop_Springboot.dto.AdminDto;
import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.dto.RoleDto;
import com.example.BookShop_Springboot.model.Category;
import com.example.BookShop_Springboot.model.Role;
import com.example.BookShop_Springboot.service.AdminService;
import com.example.BookShop_Springboot.service.RoleService;
import com.example.BookShop_Springboot.service.impl.RoleServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RoleController {

    private final AdminService adminService;
    private final RoleService roleService;
    
    @GetMapping("/roles")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }

        return "redirect:/admin/roles/0";
    }
    
    @GetMapping("/roles/{pageNo}")
    public String allRoles(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        Page<AdminDto> admins = adminService.getAllAdmins(pageNo);
        model.addAttribute("title", "Manage Roles");
        model.addAttribute("size", admins.getSize());
        model.addAttribute("admins", admins);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", admins.getTotalPages());
        
        return "roles";
    }
    
    @GetMapping("/update-user/{username}")
    public String updateProductForm(@PathVariable("username") String username, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        List<RoleDto> roles = roleService.findAll();
        AdminDto adminDto = adminService.findByUserName(username);
        System.out.println("test" + adminDto.getAddress() + ":" + adminDto.getPhone());
        model.addAttribute("title", "Add user");
        model.addAttribute("roles", roles);
        model.addAttribute("adminDto", adminDto);
        return "update-user";
    }

    @PostMapping("/update-user/{username}")
    public String updateUser(@ModelAttribute("adminDto") AdminDto adminDto,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/admin/login";
            }
            adminService.update(adminDto);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/admin/roles/0";
    }
}
