package com.ecommerce.service.implement;

import com.ecommerce.entity.Product;
import com.ecommerce.enums.Pagination;
import com.ecommerce.exception.ProductNotFoundException;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class ProductServiceImplement implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImplement(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
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

    @Override
    public Page<Product> listByCategory(int pageNum, Long categoryId) {
        String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.PRODUCT_PER_PAGE.getValue());
        return productRepository.listByCategory(categoryId, pageable, categoryIdMatch);
    }

    @Override
    public Product getProduct(String alias) throws ProductNotFoundException {
        try {
            return productRepository.findByAlias(alias);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Could not find any product with alias " + alias);
        }
    }

    @Override
    public String checkUnique(Long id, String title) {
        Product productByName = productRepository.findByTitle(title);
        if (id == null || id == 0) {
            if (productByName != null) {
                return "Duplicate";
            }
        } else {
            if (productByName != null && !Objects.equals(productByName.getId(), id)) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    @Override
    public Page<Product> search(String keyword, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.SEARCH_RESULTS_PAGE.getValue());
        return productRepository.search(keyword, pageable);
    }

    @Override
    public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword, Long categoryId) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.PRODUCTS_PER_ADMIN_PAGE.getValue(), sort);
        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
                return productRepository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            }
            return productRepository.findAll(keyword, pageable);
        }
        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        }
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        try {
            return productRepository.getReferenceById(id);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Could not find any product with id " + id);
        }
    }

    @Override
    public void saveProduct(Product product) {
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getTitle().toLowerCase();
            product.setAlias((categoryService.convertCyrillic(defaultAlias).replaceAll(" ", "_")));
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "_").toLowerCase());
        }
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        Long countById = productRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Could not find any product with ID " + id);
        }
        productRepository.deleteById(id);
    }
}
