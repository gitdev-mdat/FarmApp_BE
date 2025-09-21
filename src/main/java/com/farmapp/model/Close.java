    package com.farmapp.model;

    import com.farmapp.enums.PaymentStatus;
    import com.fasterxml.jackson.annotation.JsonFormat;
    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;
    import java.util.List;

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Table(name = "closes")
    public class Close {

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

        @Column(name="close_image_url" , length = 1000)
        private String closeImageUrl;

        @Column(name = "note", length = 1000)
        private String note;

        @OneToMany(mappedBy = "close", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
        private List<Delivery> deliveries;

        @Column(name = "created_at")
        @JsonFormat(pattern = "dd-MM-yyyy")
        private LocalDate createdAt;


        @Column(name = "is_active")
        private Boolean isActive = true;

        @PrePersist
        public void prePersist() {
            if (createdAt == null) createdAt = LocalDate.now();
            if (isActive == null) isActive = true;
        }
    }
