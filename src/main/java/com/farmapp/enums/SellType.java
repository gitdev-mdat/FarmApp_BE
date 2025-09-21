package com.farmapp.enums;

public enum SellType {
    DIRECT("Bán trực tiếp"),
    FROM_DEPOSIT("Bán từ ký gửi");

    private final String displayName;

    SellType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
