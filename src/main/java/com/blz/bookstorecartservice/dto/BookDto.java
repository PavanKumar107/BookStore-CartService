package com.blz.bookstorecartservice.dto;

import lombok.Data;

@Data
public class BookDto {
	private String bookName;
	private String bookAuthor;
	private String bookDescription;
	private long bookPrice;
	private long bookQuantity;
}
