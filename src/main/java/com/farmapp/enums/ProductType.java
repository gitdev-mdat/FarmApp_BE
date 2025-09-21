package com.farmapp.enums;

public enum ProductType {
    PEPPER("Tiêu"),
    COFFEE("Cà phê"),
    CASHEW("Điều");

    private final String displayName;

    ProductType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
