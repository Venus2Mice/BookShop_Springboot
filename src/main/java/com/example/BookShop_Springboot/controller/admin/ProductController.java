package com.example.BookShop_Springboot.controller.admin;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.model.Category;
import com.example.BookShop_Springboot.service.CategoryService;
import com.example.BookShop_Springboot.service.ProductService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductController {
    private final String redirectLoginPath = "redirect:/admin/login";
    private final String redirectProductsPath = "redirect:/admin/products/0";
    private final String productsPath = "/admin/products";
    private final String addProductPath = "/admin/add-product";
    private final String updateProductPath = "/admin/update-product";
    private final String productsResultPath = "/admin/product-result";

    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        return redirectProductsPath;
    }

    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return productsPath;
    }

    @GetMapping("/search-products/{pageNo}")
    public String searchProduct(@PathVariable("pageNo") int pageNo,
            @RequestParam(value = "keyword") String keyword,
            Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);
        model.addAttribute("title", "Result Search Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return productsResultPath;

    }

    @GetMapping("/add-product")
    public String addProductPage(Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        model.addAttribute("title", "Add Product");
        List<Category> categories = categoryService.findAllByActivatedTrue();
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", new ProductDto());
        return addProductPath;
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("productDto") ProductDto product,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            if (productService.findByName(product.getName()) == null) {
                productService.save(imageProduct, product);
                redirectAttributes.addFlashAttribute("success", "Add new product successfully!");
            }
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to add new product!");
        }
        return redirectProductsPath;
    }

    @GetMapping("/update-product/{id}/currentPage/{currentPage}")
    public String updateProductForm(@PathVariable("id") Long id, @PathVariable("currentPage") String currentPage,
            Model model, Principal principal) {
        if (principal == null) {
            return redirectLoginPath;
        }
        List<Category> categories = categoryService.findAllByActivatedTrue();
        ProductDto productDto = productService.getById(id);
        model.addAttribute("title", "Add Product");
        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);
        model.addAttribute("currentPage", currentPage);
        return updateProductPath;
    }

    @PostMapping("/update-product/{id}/currentPage/{currentPage}")
    public String updateProduct(@ModelAttribute("productDto") ProductDto productDto,
            @PathVariable("currentPage") String currentPage,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            productService.update(imageProduct, productDto);
            redirectAttributes.addFlashAttribute("success", "Update successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error server, please try again!");
        }
        return "redirect:/admin/products/" + currentPage;
    }

    @RequestMapping(value = "/enable-product", method = { RequestMethod.PUT, RequestMethod.GET })
    public String enabledProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            productService.enableById(id);
            redirectAttributes.addFlashAttribute("success", "Enabled successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Enabled failed!");
        }
        return redirectProductsPath;
    }

    @RequestMapping(value = "/delete-product", method = { RequestMethod.PUT, RequestMethod.GET })
    public String deletedProduct(Long id, RedirectAttributes redirectAttributes, Principal principal) {
        try {
            if (principal == null) {
                return redirectLoginPath;
            }
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Deleted failed!");
        }
        return redirectProductsPath;
    }
}
