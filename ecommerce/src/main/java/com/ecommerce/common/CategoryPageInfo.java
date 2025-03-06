package com.ecommerce.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CategoryPageInfo {

    private long totalElements;
    private int totalPages;

    public long getTotalElements() {
        return this.totalElements;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
