package com.ecommerce.repository;

import com.ecommerce.entity.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT category FROM Category category WHERE category.enabled = true ORDER BY category.title ASC")
    public List<Category> findAllEnabled();
}
