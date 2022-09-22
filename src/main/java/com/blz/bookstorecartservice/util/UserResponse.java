package com.blz.bookstorecartservice.util;

import com.blz.bookstorecartservice.dto.BSUserDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	  private String message;
	    private int errorCode;
	    private BSUserDto object;
	     
}
