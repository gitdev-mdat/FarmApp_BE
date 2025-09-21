package com.farmapp.enums;

public enum TransactionAction {
    CREATE("Tạo mới"),
    UPDATE("Cập nhật"),
    DELETE("Xoá");

    private final String displayName;

    TransactionAction(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
