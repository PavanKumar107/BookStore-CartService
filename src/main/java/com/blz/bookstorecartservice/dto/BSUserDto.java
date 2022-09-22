package com.blz.bookstorecartservice.dto;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BSUserDto {
	private long userId;
	private String fullName;
	private String emailId;
	private String password;
	private LocalDateTime registeredDate;
	private LocalDateTime updatedDate;
	private String dateOfBirth;
	private String profilePic;
	private boolean verify;
	private  boolean isDeleted;		
	private int otp;
	private LocalDate purchaseDate;
	private LocalDate expiryDate;
}
