package com.example.BookShop_Springboot.service;

import java.util.List;
import java.util.Set;

import com.example.BookShop_Springboot.dto.ProductDto;
import com.example.BookShop_Springboot.dto.ShoppingCartDto;
import com.example.BookShop_Springboot.model.CartItem;
import com.example.BookShop_Springboot.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart addItemToCart(ProductDto productDto, int quantity, String username);

    ShoppingCart updateCart(ProductDto productDto, int quantity, String username);

    ShoppingCart removeItemFromCart(ProductDto productDto, String username);

    ShoppingCartDto addItemToCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity);

    ShoppingCartDto updateCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity);

    ShoppingCartDto removeItemFromCartSession(ShoppingCartDto cartDto, ProductDto productDto, int quantity);

    ShoppingCart combineCart(ShoppingCartDto cartDto, ShoppingCart cart);


    void deleteCartById(Long id);

    ShoppingCart getCart(String username);

    List<CartItem> sortCartItems(Set<CartItem> cartItems);
}
