package com.ecommerce.repository;

import com.ecommerce.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("SELECT v FROM Vendor v WHERE v.title = :title")
    public Vendor findByTitle(@Param("title") String title);
    
    public Long countById(Long id);
}
