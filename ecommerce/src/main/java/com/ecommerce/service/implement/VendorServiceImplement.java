package com.ecommerce.service.implement;

import com.ecommerce.entity.Vendor;
import com.ecommerce.enums.Pagination;
import com.ecommerce.repository.VendorRepository;
import com.ecommerce.service.VendorService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

@Transactional
@Service
public class VendorServiceImplement implements VendorService {

    private final VendorRepository vendorRepository;

    public VendorServiceImplement(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public String checkVendorTitle(Long id, String title) {
        Vendor vendor = vendorRepository.findByTitle(title);
        if (id == null) {
            if (vendor != null) {
                return "Duplicate";
            }
        } else {
            if (!Objects.equals(vendor.getId(), id)) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    @Override
    public Page<Vendor> listByPage(int pageNum) {
        Pageable pageable = PageRequest.of(pageNum - 1, Pagination.VENDORS_PER_PAGE.getValue());
        return vendorRepository.findAll(pageable);
    }

    @Override
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @Override
    public void saveVendor(Vendor vendor) {
        vendorRepository.save(vendor);
    }

    @Override
    public Vendor getVendor(Long id) {
        Vendor vendor = null;
        Optional<Vendor> optional = vendorRepository.findById(id);
        if (optional.isPresent()) {
            vendor = optional.get();
        }
        return vendor;
    }
    
    @Override
    public void deleteVendor(Long id) {
        Long countById = vendorRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new NotFoundException("Could not find any vendor with id " + id);
        }
        vendorRepository.deleteById(id);
    }
}
