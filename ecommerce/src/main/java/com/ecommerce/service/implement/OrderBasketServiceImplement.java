package com.ecommerce.service.implement;

import com.ecommerce.entity.OrderBasket;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.repository.OrderBasketRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.OrderBasketService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Transactional
@Service
public class OrderBasketServiceImplement implements OrderBasketService {

    private final OrderBasketRepository orderBasketRepository;
    private final ProductRepository productRepository;

    public OrderBasketServiceImplement(OrderBasketRepository orderBasketRepository, ProductRepository productRepository) {
        this.orderBasketRepository = orderBasketRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Integer addProduct(Long productId, Integer quantity, User user) {
        Integer addedQuantity = quantity;
        Product product = productRepository.getReferenceById(productId);
        OrderBasket orderBasket = orderBasketRepository.findByUserAndProduct(user, product);
        if (orderBasket != null) {
            addedQuantity = orderBasket.getQuantity() + quantity;
            orderBasket.setQuantity(addedQuantity);
        } else {
            orderBasket = new OrderBasket();
            orderBasket.setQuantity(quantity);
            orderBasket.setUser(user);
            orderBasket.setProduct(product);
        }
        orderBasketRepository.save(orderBasket);
        return addedQuantity;
    }

    @Override
    public float updateQuantity(Long productId, Integer quantity, User user) {
        orderBasketRepository.updateQuantity(quantity, productId, user.getId());
        Product product = productRepository.getReferenceById(productId);
        return product.getPrice() * quantity;
    }

    @Override
    public void removeProduct(Long productId, User user) {
        orderBasketRepository.deleteByUserAndProduct(user.getId(), productId);
    }

    @Override
    public List<OrderBasket> getAllOrderBaskets() {
        List<OrderBasket> orderBasket = orderBasketRepository.findAll();
        if (orderBasket.isEmpty()) {
            throw new NotFoundException("Could not find any product in DB");
        }
        return orderBasket;
    }
}
