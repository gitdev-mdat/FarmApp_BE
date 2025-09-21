package com.farmapp.model;

import com.farmapp.enums.UserGender;
import com.farmapp.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;

    @Column (name = "name", length = 100)
    private String name;

    @Column (name = "avatar_url", length = 1000)
    private String avatarUrl;

    @Column(name = "phone", length = 20,unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private UserGender gender;

    @Column(name = "password")
    private String password;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "identity_card_number", length = 20)
    private String identityCardNumber;

    @Column(name = "identity_card_front_url", length = 1000)
    private String identityCardFrontUrl;

    @Column(name = "identity_card_back_url", length = 1000)
    private String identityCardBackUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "active", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean active = true;

    @PrePersist
    public void prePersist() {
        if (role == null) role = UserRole.FARMER;
    }
}
