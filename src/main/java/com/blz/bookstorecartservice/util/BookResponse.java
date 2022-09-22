package com.blz.bookstorecartservice.util;
import com.blz.bookstorecartservice.dto.BookDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
	private String message;
	private int errorCode;
	private BookDto object;
   
}

