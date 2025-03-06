package com.ecommerce.enums;

public enum Pagination {
    USER_PER_PAGE(4),
    VENDORS_PER_PAGE(4),
    ORDERS_PER_PAGE(8),
    TOP_CATEGORIES_PER_PAGE(1),
    PRODUCT_PER_PAGE(10),
    SEARCH_RESULTS_PAGE(10),
    PRODUCTS_PER_ADMIN_PAGE(5);

    private final int value;

    Pagination(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
