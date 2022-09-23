package com.blz.bookstorecartservice.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.blz.bookstorecartservice.dto.BSUserDto;
import com.blz.bookstorecartservice.dto.BookDto;
import com.blz.bookstorecartservice.dto.CartDto;
import com.blz.bookstorecartservice.exception.CustomNotFoundException;
import com.blz.bookstorecartservice.model.CartModel;
import com.blz.bookstorecartservice.repository.CartRepository;
import com.blz.bookstorecartservice.util.BookResponse;
import com.blz.bookstorecartservice.util.CartResponse;
import com.blz.bookstorecartservice.util.TokenUtil;
import com.blz.bookstorecartservice.util.UserResponse;

import lombok.extern.slf4j.Slf4j;

/**
 *  
 * Purpose:Service implementation for Cart Service
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 */

@Service
@Slf4j
public class CartService implements ICartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	TokenUtil tokenUtil;

	@Autowired
	RestTemplate restTemplate;

	
	/**
	 *  Purpose:service to add books to the cart 
	 */
	@Override
	public CartModel addToCart(String token, CartDto cartDto, Long bookId) {
		Long userId=tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BOOKSTORE-USERSERVICE:8068/userservice/validateuser/" + token, UserResponse.class);
		if (isUserPresent.getToken().getUserId() == userId) {
			log.info("abc");
			BookResponse isBookPresent = restTemplate.getForObject("http://BOOKSTORE-BOOKSERVICE:8069/bookservice/validatebookid/" + bookId, BookResponse.class);
			if (isBookPresent.getToken().getBookId() == bookId) {
				log.info("def");
				CartModel cart = new CartModel(cartDto);
				cart.setBookId(bookId);
				cart.setUserId(userId);
				if (isBookPresent.getToken().getBookQuantity()>= cartDto.getQuantity()) {
					log.info("ghi");
					cart.setQuantity(cartDto.getQuantity());
					cart.setTotalPrice((cartDto.getQuantity()) * (isBookPresent.getToken().getBookPrice()));
					cartRepository.save(cart);
					BookResponse isBooksPresent = restTemplate.getForObject("http://BOOKSTORE-BOOKSERVICE:8069/bookservice/addingtocart/" + bookId +"/"+ cartDto.getQuantity(), BookResponse.class);
					return cart;
				}
				throw new CustomNotFoundException(500,"books are out of stock");
			}
			throw new CustomNotFoundException(500, "book not available");
		}
		throw new CustomNotFoundException(500, "user not present");
	}
//		@Override
//		public CartModel addToCart(String token, CartDto cartDto, Long bookId) {
//			BookDto book = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/validatebookid/" + bookId, BookDto.class);
//			BSUserDto user = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + token, BSUserDto.class);
//			if (user.equals(null)) {
//				throw new CustomNotFoundException(500, "user Id not present");
//			} else {
//				if (book.equals(null)) {
//					throw new CustomNotFoundException(500, "book Id not present");
//				} else if (cartDto.getQuantity() <= (Long) book.getBookQuantity()) {
//					CartModel cart = new CartModel(cartDto);
//					Long totalPrice = cart.getQuantity() * book.getBookPrice();
//					cart.setTotalPrice(totalPrice);
//					cart.setQuantity(cart.getQuantity());
//					cart.setBookId(bookId);
//					cart.setUserId(user.getUserId());
//					cartRepository.save(cart);
//					BookDto isBooksPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/addingtocart/" + bookId +"/" +cart.getQuantity(), BookDto.class);
//					return cart;
//				} else {
//					throw new CustomNotFoundException(500, "Cart quantity is more then Book quantity");
//				}
//			}
//		}
	
	/**
	 *  Purpose:method to remove books from the cart
	 */
	@Override
	public CartModel removingFromCart(Long cartId, String token) {
			Long userId = tokenUtil.decodeToken(token);
			UserResponse isUserPresent = restTemplate.getForObject("http://BOOKSTORE-USERSERVICE:8068/userservice/validateuser/" + token, UserResponse.class);
			if (isUserPresent.getToken().getUserId() == userId) {
				Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
				if (isCartPresent.isPresent()) {
					if (isCartPresent.get().getUserId() == userId) {
						cartRepository.delete(isCartPresent.get());
						BookResponse isBooksPresent = restTemplate.getForObject("http://BOOKSTORE-BOOKSERVICE:8069/bookservice/removefromcart/" + isCartPresent.get().getBookId() +"/"+ isCartPresent.get().getQuantity(), BookResponse.class);
						return isCartPresent.get();
					}
					throw new CustomNotFoundException(500, "No user found");
				}
				throw new CustomNotFoundException(500, "Cart Id not available");
			}
			throw new CustomNotFoundException(500, "User not found");
		}
	/**
	 *  Purpose:method to update cart  
	 */
	@Override
	public CartModel updateCart(Long cartId, Long quantity, String token) {
		Long userId = tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BOOKSTORE-USERSERVICE:8068/userservice/validateuser/" + token, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
			if (isCartPresent.isPresent()) {
				BookResponse isBookPresent = restTemplate.getForObject("http://BOOKSTORE-BOOKSERVICE:8069/bookservice/validatebookid/" + isCartPresent.get().getBookId(), BookResponse.class);
				if (isCartPresent.get().getUserId() == userId) {
					if (isCartPresent.get().getQuantity() > quantity) {
						Long bookQuantity = isCartPresent.get().getQuantity() - quantity;
						isCartPresent.get().setQuantity(quantity);
						isCartPresent.get().setTotalPrice(quantity * isBookPresent.getToken().getBookPrice());
						cartRepository.save(isCartPresent.get());
						BookResponse isBookIdPresent = restTemplate.getForObject("http://BOOKSTORE-BOOKSERVICE:8069/bookservice/removefromcart/" + isCartPresent.get().getBookId() +"/"+ bookQuantity, BookResponse.class);
						return isCartPresent.get();
					}
					else {
						Long bookQuantity = quantity - isCartPresent.get().getQuantity();
						isCartPresent.get().setQuantity(quantity);
						isCartPresent.get().setTotalPrice(quantity * isBookPresent.getToken().getBookPrice());
						cartRepository.save(isCartPresent.get());
						BookResponse isBookIdPresent = restTemplate.getForObject("http://BOOKSTORE-BOOKSERVICE:8069/bookservice/addingtocart/" + isCartPresent.get().getBookId() +"/"+ bookQuantity, BookResponse.class);
						return isCartPresent.get();
					}
				}
			}
			throw new CustomNotFoundException(500, "Cart id invalid");
		}
		throw new CustomNotFoundException(500, "User not found");
	}

	/**
	 *  Purpose:method to fetch books from cart
	 */

	@Override
	public List<CartModel> getAllCartItems(String token) {
		Long userId = tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BOOKSTORE-USERSERVICE:8068/userservice/validateuser/" + token, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			List<CartModel> isCartPresent = cartRepository.findAll();
			if (isCartPresent.size()>0) {
				return isCartPresent;
			}
			throw new CustomNotFoundException(500, "cart is empty");
		}
		throw new CustomNotFoundException(500, "token is invalid");
	}
	
	/**
	 *  Purpose:method to fetch books from cart for specific user
	 */
	@Override
	public List<CartModel> getCartdetailsForUser(String token) {
		Long userId = tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BOOKSTORE-USERSERVICE:8068/userservice/validateuser/" + token, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			List<CartModel> isCartPresent = cartRepository.findByUserId(userId);
			if (isCartPresent.size() > 0) {
				return isCartPresent;
			}
			throw new CustomNotFoundException(500, "Cart is empty");
		}
		throw new CustomNotFoundException(500, "token is invalid");
	}
	
	/**
	 *  Purpose:method to delete cart
	 */
	
	@Override
	public CartResponse deleteCart(Long cartId) {
		Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
		if (isCartPresent.isPresent()) {
			cartRepository.delete(isCartPresent.get());
			return new CartResponse("Cart Deleted Successfully", 200, isCartPresent.get());
		}
		throw new CustomNotFoundException(500, "cart id not found");
	}

	/**
	 *  Purpose:method to validate the cartId
	 */
	@Override
	public CartResponse validateCart(Long cartId) {
		Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
		if (isCartPresent.isPresent()) {
			return new CartResponse("Cart Validate Successfully",200,isCartPresent.get());
		}
		throw new CustomNotFoundException(500, "cart id not found");
	}
}

