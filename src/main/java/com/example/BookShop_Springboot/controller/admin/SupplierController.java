package com.example.BookShop_Springboot.controller.admin;

import java.util.List;
import java.util.Optional;

import com.example.BookShop_Springboot.model.Supplier;
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

import com.example.BookShop_Springboot.service.SupplierService;

import lombok.RequiredArgsConstructor;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class SupplierController {
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String redirectSupplierPath = "redirect:/admin/supplier";
    private final String suppliersPath = "/admin/supplier";

    private final SupplierService supplierService;

    @GetMapping("/supplier")
    public String supplier(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return redirectLoginPath;
        }
        model.addAttribute("title", "Manage Supplier");
        List<Supplier> suppliers = supplierService.findALl();
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("size", suppliers.size());
        model.addAttribute("supplierNew", new Supplier());
        return suppliersPath;
    }

    @PostMapping("/save-supplier")
    public String save(@ModelAttribute("supplierNew") Supplier supplier, Model model,
                       RedirectAttributes redirectAttributes) {
        try {
            supplierService.save(supplier);
            model.addAttribute("supplierNew", supplier);
            redirectAttributes.addFlashAttribute("success", "Add successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of supplier, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            model.addAttribute("supplierNew", supplier);
            redirectAttributes.addFlashAttribute("error",
                    "Error server");
        }
        return redirectSupplierPath;
    }

    @RequestMapping(value = "/findSupplierById", method = { RequestMethod.PUT, RequestMethod.GET })
    @ResponseBody
    public Optional<Supplier> findById(Long id) {
        return supplierService.findById(id);
    }

    @GetMapping("/update-supplier")
    public String update(Supplier supplier, RedirectAttributes redirectAttributes) {
        try {
            supplierService.update(supplier);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of supplier, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error",
                    "Error from server or duplicate name of supplier, please check again!");
        }
        return redirectSupplierPath;
    }

    @RequestMapping(value = "/delete-supplier", method = { RequestMethod.GET, RequestMethod.PUT })
    public String delete(Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of supplier, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return redirectSupplierPath;
    }

    @RequestMapping(value = "/enable-supplier", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enable(Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enable successfully");
        } catch (DataIntegrityViolationException e1) {
            e1.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Duplicate name of supplier, please check again!");
        } catch (Exception e2) {
            e2.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server");
        }
        return redirectSupplierPath;
    }

}
