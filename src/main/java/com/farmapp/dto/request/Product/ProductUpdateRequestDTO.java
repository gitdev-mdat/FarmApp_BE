package com.farmapp.dto.request.Product;

import com.farmapp.enums.ProductType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductUpdateRequestDTO {
    private ProductType type;
    private String name;
    private String description;
}

