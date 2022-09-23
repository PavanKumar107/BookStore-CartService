package com.blz.bookstorecartservice.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blz.bookstorecartservice.model.CartModel;

/**
 *  
 * Purpose:Repository for the cart service
 * 
 * @author: Pavan Kumar G V 
 * @version: 4.15.1.RELEASE
 * 
 **/ 

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long> {

	List<CartModel> findByUserId(Long userId);


}
