package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.exception.ProductNotFoundException;
import java.util.List;

public interface ProductService {

    public List<Product> findAllByCategoryId(Long categoryId);

    public List<Product> getRandomAmountOfProducts() throws ProductNotFoundException;
}
