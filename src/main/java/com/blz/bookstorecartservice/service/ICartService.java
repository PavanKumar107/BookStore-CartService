package com.blz.bookstorecartservice.service;

import java.util.List;

import com.blz.bookstorecartservice.dto.CartDto;
import com.blz.bookstorecartservice.model.CartModel;
import com.blz.bookstorecartservice.util.CartResponse;

/**
 *  
 * Purpose:Cart Service Interface
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/


public interface ICartService {
	
	CartModel addToCart(String token, CartDto cartDto, Long bookId);

	CartModel removingFromCart(Long cartId, String token);

	List<CartModel> getAllCartItems(String token);

	CartModel updateCart(Long cartId, Long quantity, String token);

	CartResponse deleteCart(Long cartId);

	CartResponse validateCart(Long cartId);

	List<CartModel> getCartdetailsForUser(String token);

	
	
}
