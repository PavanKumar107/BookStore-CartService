package com.blz.bookstorecartservice.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blz.bookstorecartservice.model.CartModel;

@Repository
public interface CartRepository extends JpaRepository<CartModel, Long> {
//
//	List<CartModel> findByUserId(Long userId);

}
