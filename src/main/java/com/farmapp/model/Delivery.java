package com.farmapp.model;

import com.farmapp.enums.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

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
    @JoinColumn(name = "close_id", nullable = false)
    @JsonIgnore
    private Close close;

    @Column(name = "delivered_quantity")
    private Float deliveredQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private DeliveryStatus status;

    @Column(name = "note", length = 255)
    private String note;

    @Column(name = "created_at") //  day la ngay tao, khong phai ngay giao
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createdAt;

    @Column(name = "delivered_at") // day la ngay giao
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate deliveredAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDate.now();
        if (isActive == null) isActive = true;
    }
}
