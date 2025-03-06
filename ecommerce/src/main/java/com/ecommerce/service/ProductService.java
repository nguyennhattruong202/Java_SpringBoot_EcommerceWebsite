package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.exception.ProductNotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

    public List<Product> findAllByCategoryId(Long categoryId);

    public List<Product> getRandomAmountOfProducts() throws ProductNotFoundException;

    public Page<Product> listByCategory(int pageNum, Long categoryId);
    
    public Product getProduct(String alias) throws ProductNotFoundException;
    
    public String checkUnique(Long id, String title);
    
    public Page<Product> search(String keyword, int pageNum);
    
    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Long categoryId);
    
    public Product getProduct(Long id) throws ProductNotFoundException;
    
    public void saveProduct(Product product);
    
    public void deleteProduct(Long id) throws ProductNotFoundException;
}
