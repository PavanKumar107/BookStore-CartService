package com.blz.bookstorecartservice.util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  
 * Purpose:Response to the cart
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private String message;
    private int errorCode;
    private Object token;
    
    
	public CartResponse(String message, int errorCode) {
		super();
		this.message = message;
		this.errorCode = errorCode;
	} 
}