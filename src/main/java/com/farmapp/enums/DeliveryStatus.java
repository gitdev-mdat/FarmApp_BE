package com.farmapp.enums;

public enum DeliveryStatus {
    PENDING ("Giao thiếu"),
    COMPLETED ("Giao đủ");
    private final String displayName;

    DeliveryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
