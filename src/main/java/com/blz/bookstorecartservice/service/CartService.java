package com.blz.bookstorecartservice.service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.blz.bookstorecartservice.dto.CartDto;
import com.blz.bookstorecartservice.exception.CustomNotFoundException;
import com.blz.bookstorecartservice.model.CartModel;
import com.blz.bookstorecartservice.repository.CartRepository;
import com.blz.bookstorecartservice.util.BookResponse;
import com.blz.bookstorecartservice.util.TokenUtil;
import com.blz.bookstorecartservice.util.UserResponse;


@Service
public class CartService implements ICartService {

	@Autowired
	CartRepository cartRepository;

	@Autowired
	TokenUtil tokenUtil;

	@Autowired
	RestTemplate restTemplate;

	//	@Override
	//	public CartModel addToCart(String token, CartDto cartDto, Long bookId) {
	//		BookDto book = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/validatebookid/" + bookId, BookDto.class);
	//		BSUserDto user = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + token, BSUserDto.class);
	//		if (user.equals(null)) {
	//			throw new CustomNotFoundException(500, "user Id not present");
	//		} else {
	//			if (book.equals(null)) {
	//				throw new CustomNotFoundException(500, "book Id not present");
	//			} else if (cartDto.getQuantity() <= (Long) book.getBookQuantity()) {
	//				CartModel cart = new CartModel(cartDto);
	//				Long totalPrice = cart.getQuantity() * book.getBookPrice();
	//				cart.setTotalPrice(totalPrice);
	//				cart.setQuantity(cart.getQuantity());
	//				cart.setBookId(bookId);
	//				cart.setUserId(user.getUserId());
	//				cartRepository.save(cart);
	//				BookDto isBooksPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/addingtocart/" + bookId +"/" +cart.getQuantity(), BookDto.class);
	//				return cart;
	//			} else {
	//				throw new CustomNotFoundException(500, "Cart quantity is more then Book quantity");
	//			}
	//		}
	//	}

	//	@Override
	//	public CartModel updateCart(Long cartId, String token, CartDto cartDto) {
	//		Optional<CartModel> iscartIdPresent = cartRepository.findById(cartId);
	//		if (iscartIdPresent.isPresent()) {
	//			BookDto book = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/validatebookid/" + iscartIdPresent.get().getBookId(), BookDto.class);
	//			BSUserDto user = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + token, BSUserDto.class);
	//			if (user.equals(null)) {
	//				throw new CustomNotFoundException(500, "user Id is incorrect");
	//			} else if (iscartIdPresent.get().getUserId() == user.getUserId()) {
	//				if (cartDto.getQuantity() <= book.getBookQuantity()) {
	//					iscartIdPresent.get().setQuantity(cartDto.getQuantity());
	//					iscartIdPresent.get().setTotalPrice(book.getBookPrice() * cartDto.getQuantity());
	//					cartRepository.save(iscartIdPresent.get());
	//					return iscartIdPresent.get();
	//				}
	//				
	//			}
	//		}
	//		throw new CustomNotFoundException(500, "Cart id not fount");
	//	}
	//}
	
	/**
	 *  Purpose:service to add books to the cart 
	 */
	@Override
	public CartModel addToCart(String token, CartDto cartDto, Long bookId) {
		Long userId=tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + token, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			BookResponse isBookPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/validatebookid/" + bookId, BookResponse.class);
			if (isBookPresent.getErrorCode() == 200) {
				CartModel cart = new CartModel(cartDto);
				cart.setBookId(bookId);
				cart.setUserId(userId);
				if (isBookPresent.getObject().getBookQuantity()> cartDto.getQuantity()) {
					cart.setQuantity(cartDto.getQuantity());
					cart.setTotalPrice((cartDto.getQuantity()) * (isBookPresent.getObject().getBookPrice()));
					cartRepository.save(cart);
					BookResponse isBooksPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/addingtocart/" + bookId +"/" +cartDto.getQuantity(), BookResponse.class);
					return cart;
				}
				throw new CustomNotFoundException(500,cartDto.getQuantity()+ "out of stock");
			}
			throw new CustomNotFoundException(500, "book not available");
		}
		throw new CustomNotFoundException(500, "user not present");
	}

	/**
	 *  Purpose:method to remove books from the cart
	 */
	@Override
	public CartModel removingFromCart(Long cartId, String token) {
		Long userId = tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + userId, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
			if (isCartPresent.isPresent()) {
				if (isCartPresent.get().getUserId() == userId) {
					cartRepository.delete(isCartPresent.get());
					BookResponse isBookPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/removefromcart/" + isCartPresent.get().getBookId() +"/" +isCartPresent.get().getQuantity(), BookResponse.class);
					return isCartPresent.get();
				}
			}
			throw new CustomNotFoundException(400, "Cart Id not present");
		}
		throw new CustomNotFoundException(400, "User not present");
	}

	/**
	 *  Purpose:method to update cart  
	 */
	@Override
	public CartModel updateCart(Long cartId, Long quantity, String token) {
		Long userId = tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + userId, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			Optional<CartModel> isCartPresent = cartRepository.findById(cartId);
			if (isCartPresent.isPresent()) {
				BookResponse isBookPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/validateBookId/" + isCartPresent.get().getBookId(), BookResponse.class);
				if (isCartPresent.get().getUserId() == userId) {
					if (isCartPresent.get().getQuantity() > quantity) {
						long bookQuantity = isCartPresent.get().getQuantity() - quantity;
						isCartPresent.get().setQuantity(quantity);
						isCartPresent.get().setTotalPrice(quantity * isBookPresent.getObject().getBookPrice());
						cartRepository.save(isCartPresent.get());
						BookResponse isBookIdPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/addingtocart/" + isCartPresent.get().getBookId() +"/" +bookQuantity, BookResponse.class);
						return isCartPresent.get();
					} else {
						long bookQuantity = quantity-isCartPresent.get().getQuantity();
						isCartPresent.get().setQuantity(quantity);
						isCartPresent.get().setTotalPrice(quantity * isBookPresent.getObject().getBookPrice());
						cartRepository.save(isCartPresent.get());
						BookResponse isBookIdPresent = restTemplate.getForObject("http://BookStore-BookService:8069/bookservice/removefromcart/" + isCartPresent.get().getBookId() +"/" +bookQuantity, BookResponse.class);
						return isCartPresent.get();
					}
				}
			}
			throw new CustomNotFoundException(400, "Books not found");
		}
		throw new CustomNotFoundException(400, "Token is invalid");
	}

	/**
	 *  Purpose:service to get fetch books from cart
	 */

	@Override
	public List<CartModel> getAllCartItems(String token) {
		Long userId = tokenUtil.decodeToken(token);
		UserResponse isUserPresent = restTemplate.getForObject("http://BookStore-UserService:8068/userservice/validateuser/" + userId, UserResponse.class);
		if (isUserPresent.getErrorCode() == 200) {
			List<CartModel> isCartPresent = cartRepository.findAll();
			if (isCartPresent.size()>0) {
				return isCartPresent;
			}
			throw new CustomNotFoundException(500, "cart is empty");
		}
		throw new CustomNotFoundException(500, "token is invalid");
	}
}

