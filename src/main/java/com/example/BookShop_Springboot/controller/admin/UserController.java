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
import com.example.BookShop_Springboot.service.AdminService;
import com.example.BookShop_Springboot.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {

    private final AdminService adminService;
    private final RoleService roleService;
    
    @GetMapping("/users")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }

        return "redirect:/admin/users/0";
    }
    
    @GetMapping("/users/{pageNo}")
    public String allRoles(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        Page<AdminDto> admins = adminService.getAllAdmins(pageNo);
        model.addAttribute("title", "Manage Users");
        model.addAttribute("size", admins.getSize());
        model.addAttribute("admins", admins);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", admins.getTotalPages());
        
        return "/admin/users";
    }
    
    @GetMapping("/search-users/{pageNo}")
    public String searchUser(@PathVariable("pageNo") int pageNo,
            @RequestParam(value = "keyword") String keyword,
            Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        Page<AdminDto> admins = adminService.searchByUserName(pageNo, keyword);
        System.out.println(keyword);
        for(AdminDto item : admins) {
        	System.out.println(item.getUsername());
        }
        model.addAttribute("title", "Result Search Users");
        model.addAttribute("size", admins.getSize());
        model.addAttribute("admins", admins);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", admins.getTotalPages());
        return "/admin/user-result";
    }
    
    @GetMapping("/add-user")
    public String addUser(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("title", "Add User");
        List<RoleDto> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        model.addAttribute("adminDto", new AdminDto());
        return "/admin/add-user";
    }
    
    @PostMapping("/add-user")
    public String saveProduct(@ModelAttribute("adminDto") AdminDto admin,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/admin/login";
            }
            if (adminService.findByUserName(admin.getUsername()) == null && adminService.findByEmail(admin.getEmail()) == null) {
                admin.setPassword("123456");
            	adminService.save(admin);
                redirectAttributes.addFlashAttribute("success", "Add new user successfully!");
            }
            else {
            	redirectAttributes.addFlashAttribute("error", "Failed to add new user!");            	
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new user!");
        }
        return "redirect:/admin/users/0";
    }
    
    @GetMapping("/update-user/{username}")
    public String updateProductForm(@PathVariable("username") String username, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/admin/login";
        }
        List<RoleDto> roles = roleService.findAll();
        AdminDto adminDto = adminService.findByUserName(username);
        model.addAttribute("title", "Add user");
        model.addAttribute("roles", roles);
        model.addAttribute("adminDto", adminDto);
        return "/admin/update-user";
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
        return "redirect:/admin/users/0";
    }
    

    @RequestMapping(value = "/enable-user", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enabledProduct(String username, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/admin/login";
            }
            adminService.enableByUsername(username);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return "redirect:/admin/users/0";
    }

    @RequestMapping(value = "/disable-user", method = { RequestMethod.PUT, RequestMethod.GET })
    public String deletedProduct(String username, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return "redirect:/admin/login";
            }
            adminService.disableByUsername(username);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return "redirect:/admin/users/0";
    }
}
