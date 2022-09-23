package com.blz.bookstorecartservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blz.bookstorecartservice.dto.CartDto;
import com.blz.bookstorecartservice.model.CartModel;
import com.blz.bookstorecartservice.service.ICartService;
import com.blz.bookstorecartservice.util.CartResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
/**
 * Purpose: Cart Controller to process Cart Data APIs.
 * @version: 4.15.1.RELEASE
 * @author: Pavan Kumar G V
 *   
 */

@RestController
@RequestMapping("/cartservice")
public class CartController {

	@Autowired
	ICartService cartService;

	/**
	 * Purpose: add to cart
	 * @Param: token,cartDto,bookId
	 * 
	 */
	@PostMapping("/addtocart/{bookId}")
	public ResponseEntity<CartResponse> addToCart(@RequestHeader String token,@RequestBody CartDto cartDto,@RequestParam Long bookId) {
		CartModel cartModel = cartService.addToCart(token,cartDto,bookId);
		CartResponse response = new CartResponse("book added to cart sucessfully ", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

	/**
	 * Purpose: update cart
	 * @Param: cartId,token,quantity
	 * 
	 */
	@PutMapping("/updatecart/{cartId}")
	public ResponseEntity<CartResponse> updateCart(@PathVariable Long cartId,@RequestHeader String token,@RequestParam  Long quantity) {
		CartModel cartModel = cartService.updateCart(cartId,quantity,token);
		CartResponse response = new CartResponse("cart updated sucessfully ", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

	/**
	 * Purpose: removing book from cart
	 * @Param: token ,cartId
	 * 
	 */
	@DeleteMapping("/removingfromcart/{cartId}")
	public ResponseEntity<CartResponse> removingFromCart(@PathVariable Long cartId,@RequestHeader String token) {
		CartModel cartModel = cartService.removingFromCart(cartId,token);
		CartResponse response = new CartResponse("book deleted from cart sucessfully ", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@PutMapping("/getusercartdetails")
//	public ResponseEntity<Response> getCartDetailsForUser(@RequestHeader String token){
//		List<CartModel> cartModel = cartService.getCartDetailsForUser(token);
//		Response response = new Response("fetching cart details for user sucessfully ", 200, cartModel);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	/**
	 * Purpose: to fetch all cart items
	 * @Param: token
	 * 
	 */
	@PutMapping("/getallcartitems")
	public ResponseEntity<CartResponse> getAllCartItems(@RequestHeader String token) {
		List<CartModel> cartModel = cartService.getAllCartItems(token);
		CartResponse response = new CartResponse("fetching all items from cart", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose: to fetch cart details for specific user
	 * @Param: token
	 * 
	 */
	@GetMapping("/getcartdetailsforuser")
	public ResponseEntity<CartResponse> getCartdetailsForUser(@RequestHeader String token) {
		List<CartModel> cartModel = cartService.getCartdetailsForUser(token);
		CartResponse response = new CartResponse("fetching cart items for user", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Purpose: to delete the cart
	 * @Param: cartId
	 * 
	 */
	@DeleteMapping("/deletecart/{cartId}")
	public CartResponse deleteCart(@PathVariable Long cartId) {
		return cartService.deleteCart(cartId);
	}
	
	/**
	 * Purpose: to validate the cart
	 * @Param: cartId
	 * 
	 */
	@GetMapping("/validatecart/{cartId}")
	public CartResponse validateCart(@PathVariable Long cartId) {
		return cartService.validateCart(cartId);
	}
}