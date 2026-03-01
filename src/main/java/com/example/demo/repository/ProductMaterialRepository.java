package com.example.demo.repository;

import com.example.demo.model.ProductMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductMaterialRepository extends JpaRepository<ProductMaterial, Long> {

    List<ProductMaterial> findByProductId(Long productId);
}