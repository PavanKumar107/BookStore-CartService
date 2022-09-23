package com.blz.bookstorecartservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blz.bookstorecartservice.dto.CartDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *  
 * Purpose:model for cart service
 * 
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long cartId;
	private long userId; 
	private long bookId; 
	private long quantity;
	private long totalPrice;
	
	
	public CartModel(CartDto cartDto) {
		this.quantity = cartDto.getQuantity();
	}
}
