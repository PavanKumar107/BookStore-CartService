package com.blz.bookstorecartservice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blz.bookstorecartservice.dto.CartDto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
public class CartModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long cartId;
	private long UserId; 
	private long BookId; 
	private long quantity;
	private long totalPrice;
	
	
	public CartModel(CartDto cartDto) {
		this.quantity = cartDto.getQuantity();
	}
}
