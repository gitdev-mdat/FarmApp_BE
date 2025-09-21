package com.farmapp.enums;

public enum TransactionType {

    CLOSE("Chốt"),
    SELL_FROM_DEPOSIT("Bán từ kí gửi"),
    SELL_DIRECT("Bán truc tiep");
    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
