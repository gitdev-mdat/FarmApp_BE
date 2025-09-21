package com.farmapp.repository;

import com.farmapp.enums.ProductType;
import com.farmapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findById(Integer id);
    Optional<Product> findByType(ProductType type); // Tốt nếu type là unique
    List<Product> findAllByType(ProductType type);  // Thêm cái này để dùng getAllProductsByType
}

