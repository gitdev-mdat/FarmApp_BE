package com.farmapp.model;

import com.farmapp.enums.PaymentStatus;
import com.farmapp.enums.SellType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "sells")
public class Sell {

    @Id
    @Column(name = "id")
    private String id;
    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "deposit_id", nullable = true)
    private Deposit deposit;

    @Column(name = "total_quantity")
    private Float totalQuantity;

    @Column(name = "price")
    private Integer price;

    @Column(name = "amount_due")    
    private Integer amountDue;  // Tổng cần thanh toán = totalQuantity * price

    @Column(name = "amount_paid")
    private Integer amountPaid;  // Tổng đã thanh toán

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatus paymentStatus;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "sell_type", length = 20)
    private SellType sellType;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "sell_image_url", length = 1000)
    private String sellImageUrl;
    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
        if (isActive == null) isActive = true;
    }
}
