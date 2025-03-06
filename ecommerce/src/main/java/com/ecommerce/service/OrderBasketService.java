package com.ecommerce.service;

import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import java.util.List;

public interface OrderBasketService {

    public Integer addProduct(Long productId, Integer quantity, User user);

    public float updateQuantity(Long productId, Integer quantity, User user);

    public void removeProduct(Long productId, User user);

    public List<OrderBasket> getAllOrderBaskets();
}
