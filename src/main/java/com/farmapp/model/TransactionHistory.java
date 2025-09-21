package com.farmapp.model;

import com.farmapp.enums.TransactionAction;
import com.farmapp.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "transaction_history")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 100)
    private TransactionType type;

    @Column(name = "ref_id", length = 100)
    private String refId; // mã đơn: D001, C001, S001,...

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 10)
    private TransactionAction action;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // người thực hiện

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
