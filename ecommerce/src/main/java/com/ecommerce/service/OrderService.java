package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import com.ecommerce.exception.OrderNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;

public interface OrderService {

    public List<Order> getAllOrdersByUser(User user);

    public float countSum(List<OrderBasket> orderBaskets);
    
    public void saveOrder(Order orders);
    
    public Page<Order> listByPage(int pageNum);
    
    public Order getOrder(Long id);
    
    public void deleteOrder(Long id) throws OrderNotFoundException;
}
