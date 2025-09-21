package com.farmapp.service.impl;

import com.farmapp.dto.request.Product.ProductCreateRequestDTO;
import com.farmapp.dto.request.Product.ProductUpdateRequestDTO;
import com.farmapp.dto.response.Product.ProductResponseDTO;
import com.farmapp.enums.ProductType;
import com.farmapp.mapper.ProductMapper;
import com.farmapp.model.Product;
import com.farmapp.repository.ProductRepository;
import com.farmapp.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductCreateRequestDTO dto) {
        Product product = productMapper.toEntity(dto);
        product = productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponseDTO> getAllProductsByType(ProductType type) {
        return productRepository.findAllByType(type)
                .stream()
                .map(productMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Integer id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponseDTO updateProduct(Integer id, ProductUpdateRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        productMapper.updateEntity(product, dto);
        product = productRepository.save(product);
        return productMapper.toResponseDTO(product);
    }
}
