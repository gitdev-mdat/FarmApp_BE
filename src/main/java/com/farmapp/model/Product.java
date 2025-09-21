package com.farmapp.model;

import com.farmapp.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY )
    @Column (name = "id")
    private int id;

    @Column (name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column (name = "type")
    private ProductType type;

    @Column (name = "description")
    private String description;


}
