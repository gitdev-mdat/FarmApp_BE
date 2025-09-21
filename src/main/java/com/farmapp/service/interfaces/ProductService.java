package com.farmapp.service.interfaces;

import com.farmapp.enums.ProductType;
import com.farmapp.model.Product;

import java.util.List;

import com.farmapp.dto.request.Product.ProductCreateRequestDTO;
import com.farmapp.dto.request.Product.ProductUpdateRequestDTO;
import com.farmapp.dto.response.Product.ProductResponseDTO;

public interface ProductService {
    ProductResponseDTO createProduct(ProductCreateRequestDTO dto);
    List<ProductResponseDTO> getAllProducts();
    List<ProductResponseDTO> getAllProductsByType(ProductType type);
    void deleteProduct(Integer id);
    ProductResponseDTO updateProduct(Integer id, ProductUpdateRequestDTO dto);
}
