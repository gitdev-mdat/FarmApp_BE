package com.farmapp.enums;

public enum PaymentType {
    CASH("Tiền mặt"),
    BANK_TRANSFER("Chuyển khoản"),
    OTHER("Khác");

    private final String displayName;

    PaymentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
