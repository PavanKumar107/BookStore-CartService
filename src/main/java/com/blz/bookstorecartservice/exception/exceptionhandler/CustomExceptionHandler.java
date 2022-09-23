package com.blz.bookstorecartservice.exception.exceptionhandler;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.blz.bookstorecartservice.exception.CustomNotFoundException;
import com.blz.bookstorecartservice.util.CartResponse;

@ControllerAdvice
public class CustomExceptionHandler {
	@ExceptionHandler(CustomNotFoundException.class)
	public ResponseEntity<CartResponse> handleHiringException(CustomNotFoundException he){
		CartResponse response=new CartResponse();
		response.setErrorCode(400);
		response.setMessage(he.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
//	@ExceptionHandler(value = Exception.class)
//    public ResponseEntity<CartResponse> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
//		CartResponse Response = new CartResponse();
//		Response.setErrorCode(500);
//		Response.setMessage(e.getMessage());
//        return new ResponseEntity<CartResponse>(Response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}