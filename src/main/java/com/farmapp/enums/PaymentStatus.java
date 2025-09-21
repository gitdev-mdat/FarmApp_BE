package com.farmapp.enums;

public enum PaymentStatus {

    UNPAID("Chưa thanh toán"),           // Thêm trạng thái chưa trả đồng nào
    PARTIALLY_PAID("Thanh toán một phần"), // Đã trả nhưng chưa đủ
    PAID("Đã thanh toán đầy đủ");          // Đã trả đủ

    private final String displayName;

    PaymentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
