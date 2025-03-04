package com.ecommerce.service.implement;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class OrderServiceImplement implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImplement(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrdersByUser(User user) {
        return orderRepository.findOrderByUser(user);
    }

    @Override
    public float countSum(List<OrderBasket> orderBaskets) {
        float sum = 0;
        for (OrderBasket orderBasket : orderBaskets) {
            sum += orderBasket.getSubtotal();
        }
        return sum;
    }

    @Override
    public void saveOrder(Order orders) {
        orderRepository.save(orders);
    }
}
