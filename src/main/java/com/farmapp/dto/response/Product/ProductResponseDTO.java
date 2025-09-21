package com.farmapp.dto.response.Product;

import com.farmapp.enums.ProductType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private Integer id;
    private String name;
    private ProductType type;
    private String description;
}
