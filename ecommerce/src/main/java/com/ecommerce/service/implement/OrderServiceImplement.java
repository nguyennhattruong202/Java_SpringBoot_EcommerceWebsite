package com.ecommerce.service.implement;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.User;
import com.ecommerce.enums.Pagination;
import com.ecommerce.exception.OrderNotFoundException;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.OrderService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Order> listByPage(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.ORDERS_PER_PAGE.getValue());
        return orderRepository.findAll(pageable);
    }

    @Override
    public Order getOrder(Long id) {
        Order orders = null;
        Optional<Order> optional = orderRepository.findById(id);
        if (optional.isPresent()) {
            orders = optional.get();
        }
        return orders;
    }

    @Override
    public void deleteOrder(Long id) throws OrderNotFoundException {
        Long countById = orderRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new OrderNotFoundException("Could not find any orders with id " + id);
        }
        orderRepository.deleteById(id);
    }
}
