package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import java.util.List;

public interface OrderService {

    public List<Order> getAllOrdersByUser(User user);

    public float countSum(List<OrderBasket> orderBaskets);
    
    public void saveOrder(Order orders);
}
