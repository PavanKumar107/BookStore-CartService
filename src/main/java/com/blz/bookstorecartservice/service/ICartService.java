package com.blz.bookstorecartservice.service;

import java.util.List;

import com.blz.bookstorecartservice.dto.CartDto;
import com.blz.bookstorecartservice.model.CartModel;

public interface ICartService {
	
	CartModel addToCart(String token, CartDto cartDto, Long bookId);

	CartModel removingFromCart(Long cartId, String token);
//
//	List<CartModel> getCartDetailsForUser(String token);

	List<CartModel> getAllCartItems(String token);

	CartModel updateCart(Long cartId, Long quantity, String token);
}
