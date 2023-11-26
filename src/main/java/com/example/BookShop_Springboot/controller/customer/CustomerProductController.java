package com.example.BookShop_Springboot.controller.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.BookShop_Springboot.model.Category;
import com.example.BookShop_Springboot.dto.CategoryDto;
import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.service.CategoryService;
import com.example.BookShop_Springboot.service.ProductService;

@Controller
@RequiredArgsConstructor
public class CustomerProductController {
    private final String productsPath = "/shop/product_list";

    private final ProductService productService;

    private final CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("page", "Products");
        model.addAttribute("title", "Menu");
        List<ProductDto> products = productService.products();
        model.addAttribute("products", products);
        return productsPath;
    }

    @GetMapping("/products/{pageNo}")
    public String allProducts(@PathVariable("pageNo") int pageNo, Model model) {
        Page<ProductDto> products = productService.getAllProducts(pageNo);
        model.addAttribute("title", "Manage Products");
        model.addAttribute("size", products.getSize());
        model.addAttribute("products", products);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", products.getTotalPages());
        return productsPath;
    }

    @GetMapping("/product-detail/{id}")
    public String details(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.getById(id);
        List<ProductDto> productDtoList = productService.findAllByCategory(product.getCategory().getName());
        model.addAttribute("products", productDtoList);
        model.addAttribute("title", "Product Detail");
        model.addAttribute("page", "Product Detail");
        model.addAttribute("productDetail", product);
        model.addAttribute("decs", product.getDescription().split("\n"));
        return "/shop/product-detail";
    }

    // @GetMapping("/shop-detail")
    // public String shopDetail(Model model) {
    //     List<CategoryDto> categories = categoryService.getCategoriesAndSize();
    //     model.addAttribute("categories", categories);
    //     List<ProductDto> products = productService.randomProduct();
    //     List<ProductDto> listView = productService.listViewProducts();
    //     model.addAttribute("productViews", listView);
    //     model.addAttribute("title", "Shop Detail");
    //     model.addAttribute("page", "Shop Detail");
    //     model.addAttribute("products", products);
    //     return "shop-detail";
    // }

    @GetMapping("/high-price")
    public String filterHighPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.filterHighProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("productViews", listView);
        model.addAttribute("products", products);
        return "shop-detail";
    }

    @GetMapping("/lower-price")
    public String filterLowerPrice(Model model) {
        List<CategoryDto> categories = categoryService.getCategoriesAndSize();
        model.addAttribute("categories", categories);
        List<ProductDto> products = productService.filterLowerProducts();
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("title", "Shop Detail");
        model.addAttribute("page", "Shop Detail");
        model.addAttribute("products", products);
        return "shop-detail";
    }

    @GetMapping("/find-products/{id}")
    public String productsInCategory(@PathVariable("id") Long id, Model model) {
        List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        List<ProductDto> productDtos = productService.findByCategoryId(id);
        List<ProductDto> listView = productService.listViewProducts();
        model.addAttribute("productViews", listView);
        model.addAttribute("categories", categoryDtos);
        model.addAttribute("title", productDtos.get(0).getCategory().getName());
        model.addAttribute("page", "Product-Category");
        model.addAttribute("products", productDtos);
        return "products";
    }

    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("keyword") String keyword, Model model) {
        // List<CategoryDto> categoryDtos = categoryService.getCategoriesAndSize();
        // List<ProductDto> productDtos = productService.searchProducts(keyword);
        // List<ProductDto> listView = productService.listViewProducts();
        // model.addAttribute("productViews", listView);
        // model.addAttribute("categories", categoryDtos);
        // model.addAttribute("title", "Search Products");
        // model.addAttribute("page", "Result Search");
        // model.addAttribute("products", productDtos);
         model.addAttribute("currentPage", 1);
        
        return "/shop/search";
    }
}
