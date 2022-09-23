package com.blz.bookstorecartservice.dto;

import javax.persistence.Column;

import lombok.Data;

/**
 *  
 * Purpose:DTO for the bookstore book data
 * 
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 
@Data
public class BookDto {
	private long bookId;
	private String bookName;
	private String bookAuthor;
	private String bookDescription;
	@Column(length = 1000)
	private String bookLogo;
	private long bookPrice;
	private long bookQuantity;
}
