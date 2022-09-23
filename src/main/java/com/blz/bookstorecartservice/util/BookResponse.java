package com.blz.bookstorecartservice.util;
import com.blz.bookstorecartservice.dto.BookDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  
 * Purpose:Response to the user
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String message;
    private int errorCode;
    private BookDto token;
}