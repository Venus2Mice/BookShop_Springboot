package com.example.BookShop_Springboot.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.model.Product;
import com.example.BookShop_Springboot.repository.ProductRepository;
import com.example.BookShop_Springboot.service.ProductService;
import com.example.BookShop_Springboot.utils.ImageUpload;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final ImageUpload imageUpload;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductDto> products() {
        return transferData(productRepository.getAllProduct());
    }

    @Override
    public List<ProductDto> allProduct() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = transferData(products);
        return productDtos;
    }

    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public Product save(MultipartFile imageProduct, ProductDto productDto) {
        Product product = new Product();
        try {
            if (imageProduct == null) {
                product.setImage(null);
            } else {
                imageUpload.uploadFile(imageProduct);
                product.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
            }
            product.setName(productDto.getName());
            product.setAuthors(productDto.getAuthors());
            product.setPublisher(productDto.getPublisher());
            product.setDescription(productDto.getDescription());
            product.setCurrentQuantity(0);
            product.setCategory(productDto.getCategory());
            product.setSalePrice(productDto.getSalePrice());
            product.setDiscount(productDto.getDiscount());
            product.setDeleted(false);
            product.setActivated(true);
            return productRepository.save(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @CacheEvict(cacheNames = "products", allEntries = true)
    public Product update(MultipartFile imageProduct, ProductDto productDto) {
        try {
            Product productUpdate = productRepository.getReferenceById(productDto.getId());
            if (imageProduct.getBytes().length > 0) {
                if (imageUpload.checkExist(imageProduct)) {
                    productUpdate.setImage(productUpdate.getImage());
                } else {
                    imageUpload.uploadFile(imageProduct);
                    productUpdate.setImage(Base64.getEncoder().encodeToString(imageProduct.getBytes()));
                }
            }
            productUpdate.setCategory(productDto.getCategory());
            productUpdate.setId(productUpdate.getId());
            productUpdate.setAuthors(productDto.getAuthors());
            productUpdate.setPublisher(productDto.getPublisher());
            productUpdate.setName(productDto.getName());
            productUpdate.setDescription(productDto.getDescription());
            productUpdate.setSalePrice(productDto.getSalePrice());
            productUpdate.setDiscount(productDto.getDiscount());
            return productRepository.save(productUpdate);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void enableById(Long id) {
        Product product = productRepository.getReferenceById(id);
        product.setActivated(true);
        product.setDeleted(false);
        productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        Product product = productRepository.getReferenceById(id);
        product.setDeleted(true);
        product.setActivated(false);
        productRepository.save(product);
    }

    @Override
    public ProductDto getById(Long id) {
        ProductDto productDto = new ProductDto();
        Product product = productRepository.getReferenceById(id);
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setSalePrice(product.getSalePrice());
        productDto.setDiscount(product.getDiscount());
        productDto.setDiscountPrice(product.getSalePrice() * (1 - product.getDiscount()/100));
        productDto.setCurrentQuantity(product.getCurrentQuantity());
        productDto.setCategory(product.getCategory());
        productDto.setImage(product.getImage());
        productDto.setAuthors(product.getAuthors());
        productDto.setPublisher(product.getPublisher());
        return productDto;
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<ProductDto> randomProduct() {
        return transferData(productRepository.randomProduct());
    }

    @Override
    public Page<ProductDto> searchProducts(int pageNo, String keyword) {
        List<Product> products = productRepository.findAllByNameOrDescription(keyword);
        List<ProductDto> productDtoList = transferData(products);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<ProductDto> dtoPage = toPage(productDtoList, pageable);
        return dtoPage;
    }

    @Override
    @Cacheable(value = "products")
    public Page<ProductDto> getAllProducts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        List<ProductDto> productDtoLists = this.allProduct();
        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
        return productDtoPage;
    }

    @Override
    public Page<ProductDto> getAllProductsForCustomer(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 8);
        List<ProductDto> productDtoLists = this.allProduct();
        Page<ProductDto> productDtoPage = toPage(productDtoLists, pageable);
        return productDtoPage;
    }

    @Override
    public List<ProductDto> findAllByCategory(String category) {
        return transferData(productRepository.findAllByCategory(category));
    }

    @Override
    public List<ProductDto> filterHighProducts() {
        return transferData(productRepository.filterHighProducts());
    }

    @Override
    public List<ProductDto> filterLowerProducts() {
        return transferData(productRepository.filterLowerProducts());
    }

    @Override
    public List<ProductDto> listViewProducts() {
        return transferData(productRepository.listViewProduct());
    }

    @Override
    public List<ProductDto> findByCategoryId(Long id) {
        return transferData(productRepository.getProductByCategoryId(id));
    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        return transferData(productRepository.searchProducts(keyword));
    }

    private Page toPage(List list, Pageable pageable) {
        if (pageable.getOffset() >= list.size()) {
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }

    private List<ProductDto> transferData(List<Product> products) {
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setCurrentQuantity(product.getCurrentQuantity());
            productDto.setSalePrice(product.getSalePrice());
            productDto.setDiscount(product.getDiscount());
            productDto.setDiscountPrice(product.getSalePrice() * (1 - product.getDiscount()/100));
            productDto.setDescription(product.getDescription());
            productDto.setImage(product.getImage());
            productDto.setCategory(product.getCategory());
            productDto.setActivated(product.isActivated());
            productDto.setDeleted(product.isDeleted());
            productDto.setAuthors(product.getAuthors());
            productDto.setPublisher(product.getPublisher());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @Override
    public Object findByName(String name) {
        return productRepository.findByName(name);
    }
}
