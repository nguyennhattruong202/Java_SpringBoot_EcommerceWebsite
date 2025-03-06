package com.ecommerce.repository;

import com.ecommerce.entity.Category;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT category FROM Category category WHERE category.enabled = true ORDER BY category.title ASC")
    public List<Category> findAllEnabled();

    @Query("SELECT category from Category category WHERE category.enabled = true AND category.alias = ?1")
    public Category findByAliasEnable(String alias);

    @Query("SELECT c FROM Category c WHERE c.title = :title")
    public Category findByTitle(@Param("title") String title);

    public Category findByAlias(String alias);

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public List<Category> findRootCategories();

    @Query("SELECT c FROM Category c WHERE c.parent.id is NULL")
    public Page<Category> findRootCategories(Pageable pageable);
    
    public Long countById(Long id);
}
