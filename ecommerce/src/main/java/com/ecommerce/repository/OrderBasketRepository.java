package com.ecommerce.repository;

import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderBasketRepository extends JpaRepository<OrderBasket, Long> {

    public OrderBasket findByUserAndProduct(User user, Product product);

    @Query("UPDATE OrderBasket orderBasket SET orderBasket.quantity = ?1 WHERE orderBasket.product.id = ?2 AND orderBasket.user.id = ?3")
    @Modifying
    public void updateQuantity(Integer quantity, Long productId, Long userId);
    
    @Query("DELETE FROM OrderBasket orderBasket WHERE orderBasket.user.id = ?1 AND orderBasket.product.id = ?2")
    @Modifying
    public void deleteByUserAndProduct(Long userId, Long productId);
}
