package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    public List<Product> findAllByCategoryId(Long categoryId);

    @Query("SELECT product FROM Product product WHERE (product.category.id = ?1 OR product.category.allParentsIDs LIKE %?2%) ORDER BY product.title ASC")
    public Page<Product> listByCategory(Long categoryId, Pageable pageable, String categoryIdMatch);

    public Product findByAlias(String alias);

    public Product findByTitle(String title);

    @Query(value = "SELECT * FROM product WHERE MATCH(title, description) AGAINST (?1)", nativeQuery = true)
    public Page<Product> search(String keyword, Pageable pageable);

    public Product getReferenceById(Long productId);

    @Query("SELECT p FROM Product p WHERE (p.category.id = ?1 OR p.category.allParentsIDs LIKE %?2%) AND (p.title LIKE %?3% OR p.description LIKE %?3% OR p.vendor.title LIKE %?3% OR p.category.title LIKE %?3%)")
    public Page<Product> searchInCategory(Long categoryId, String categoryIdMatch, String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.title LIKE %?1% OR p.description LIKE %?1% OR p.vendor.title LIKE %?1% OR p.category.title LIKE %?1%")
    public Page<Product> findAll(String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.category.id = ?1 OR p.category.allParentsIDs LIKE %?2%")
    public Page<Product> findAllInCategory(Long categoryId, String categoryIdMatch, Pageable pageable);
    
    public Long countById(Long id);
}
