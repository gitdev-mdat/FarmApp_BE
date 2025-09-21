package com.farmapp.model;

import com.farmapp.enums.SeasonStatus;
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
@Table(name = "seasons")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;


    @Column(name = "end_date",nullable = true)
    private LocalDate endDate;  // LocalDate dùng để map với Date trong SQL k bao gồm time

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SeasonStatus status;

    @Column(name = "created_at")
    private LocalDate createdAt;


    @Column(name = "is_active")
    private Boolean isActive = true;
    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }

}
