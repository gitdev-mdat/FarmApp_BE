package com.farmapp.model;

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

    @Column(name = "fake_name", length = 100)
    private String fakeName;

    @Column(name = "phone", length = 20,unique = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "identity_card", length = 20)
    private String identityCard;

    @Column(name = "identity_card_url", length = 1000)
    private String identityCardUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "active")
    private boolean active;

    @PrePersist
    public void prePersist() {
        if (role == null) role = UserRole.FARMER;
    }
}
