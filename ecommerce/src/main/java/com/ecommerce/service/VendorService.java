package com.ecommerce.service;

import com.ecommerce.entity.Vendor;
import java.util.List;
import org.springframework.data.domain.Page;

public interface VendorService {

    public String checkVendorTitle(Long id, String title);

    public Page<Vendor> listByPage(int pageNum);

    public List<Vendor> getAllVendors();
    
    public void saveVendor(Vendor vendor);
    
    public Vendor getVendor(Long id);
    
    public void deleteVendor(Long id);
}
