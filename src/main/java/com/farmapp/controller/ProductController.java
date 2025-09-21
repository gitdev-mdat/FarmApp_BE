package com.farmapp.controller;

import com.farmapp.dto.request.Product.ProductCreateRequestDTO;
import com.farmapp.dto.request.Product.ProductUpdateRequestDTO;
import com.farmapp.dto.response.Product.ProductResponseDTO;
import com.farmapp.dto.response.common.GlobalResponse;
import com.farmapp.enums.ProductType;
import com.farmapp.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<GlobalResponse<ProductResponseDTO>> createProduct(
            @RequestBody ProductCreateRequestDTO dto
    ) {
        ProductResponseDTO created = productService.createProduct(dto);
        return ResponseEntity.ok(
                GlobalResponse.<ProductResponseDTO>builder()
                        .status(201)
                        .message("Tạo sản phẩm thành công")
                        .data(created)
                        .build()
        );
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<GlobalResponse<List<ProductResponseDTO>>> getAllProducts() {
        List<ProductResponseDTO> list = productService.getAllProducts();
        return ResponseEntity.ok(
                GlobalResponse.<List<ProductResponseDTO>>builder()
                        .status(200)
                        .message("Lấy danh sách sản phẩm thành công")
                        .data(list)
                        .build()
        );
    }

    // ✅ GET BY TYPE
    @GetMapping("/type")
    public ResponseEntity<GlobalResponse<List<ProductResponseDTO>>> getProductsByType(
            @RequestParam ProductType type
    ) {
        List<ProductResponseDTO> list = productService.getAllProductsByType(type);
        return ResponseEntity.ok(
                GlobalResponse.<List<ProductResponseDTO>>builder()
                        .status(200)
                        .message("Lấy sản phẩm theo loại thành công")
                        .data(list)
                        .build()
        );
    }

    // ✅ UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<GlobalResponse<ProductResponseDTO>> updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductUpdateRequestDTO dto
    ) {
        ProductResponseDTO updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(
                GlobalResponse.<ProductResponseDTO>builder()
                        .status(200)
                        .message("Cập nhật sản phẩm thành công")
                        .data(updated)
                        .build()
        );
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<String>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                GlobalResponse.<String>builder()
                        .status(200)
                        .message("Xoá sản phẩm thành công")
                        .data(null)
                        .build()
        );
    }
}
