package com.blz.bookstorecartservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.blz.bookstorecartservice.util.Response;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/cartservice")
public class CartController {

	@Autowired
	ICartService cartService;

	@PostMapping("/addtocart/{bookId}")
	public ResponseEntity<Response> addToCart(@RequestHeader String token,@RequestBody CartDto cartDto,@PathVariable Long bookId) {
		CartModel cartModel = cartService.addToCart(token,cartDto,bookId);
		Response response = new Response("book added to cart sucessfully ", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

	@PutMapping("/updatecart/{cartId}")
	public ResponseEntity<Response> updateCart(@PathVariable Long cartId,@RequestHeader String token,@RequestParam  Long quantity) {
		CartModel cartModel = cartService.updateCart(cartId,quantity,token);
		Response response = new Response("book updated to cart sucessfully ", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

	@DeleteMapping("/removingfromcart/{cartId}")
	public ResponseEntity<Response> removingFromCart(@PathVariable Long cartId,@RequestHeader String token) {
		CartModel cartModel = cartService.removingFromCart(cartId,token);
		Response response = new Response("book deleted from cart sucessfully ", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

//	@PutMapping("/getusercartdetails")
//	public ResponseEntity<Response> getCartDetailsForUser(@RequestHeader String token){
//		List<CartModel> cartModel = cartService.getCartDetailsForUser(token);
//		Response response = new Response("fetching cart details for user sucessfully ", 200, cartModel);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	@PutMapping("/getallcartitems")
	public ResponseEntity<Response> getAllCartItems(@RequestHeader String token) {
		List<CartModel> cartModel = cartService.getAllCartItems(token);
		Response response = new Response("fetching all items from cart", 200, cartModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}