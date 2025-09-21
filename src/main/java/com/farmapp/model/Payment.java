package com.farmapp.model;

import com.farmapp.enums.*;
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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "farmer_id", nullable = false)
    private User farmer;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "transaction_id")
    private String transactionId; // là id của đơn chốt (C001) hoặc đơn bán (S001)


    @Column(name= "payment_amount")
    private Integer paymentAmount; // Số tiền đã thanh toán 1 lần


    @Column(name = "payment_date")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private PaymentType type;

    @Column(name = "payment_image_url", length = 1000)
    private String paymentImageUrl;

    @Column(name = "note")
    private String note;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
    }
}
