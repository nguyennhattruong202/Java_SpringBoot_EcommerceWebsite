package com.ecommerce.service.implement;

import com.ecommerce.entity.Product;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ProductServiceImplement implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImplement(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAllByCategoryId(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    @Override
    public List<Product> getRandomAmountOfProducts() throws ProductNotFoundException {
        List<Product> productList = this.findAllByCategoryId(4L);
        if (productList.isEmpty()) {
            throw new ProductNotFoundException("Could not find any product");
        }
        Collections.shuffle(productList);
        int randomSeriesLength = 8;
        return productList.subList(0, randomSeriesLength);
    }
}
