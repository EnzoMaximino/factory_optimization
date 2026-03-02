package com.example.demo.repository;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("""
    SELECT DISTINCT p
    FROM Product p
    JOIN FETCH p.composition pm
    JOIN FETCH pm.material m
    WHERE LOWER(m.name) LIKE LOWER(CONCAT('%', :materialName, '%'))
    """)
    List<Product> findByMaterialNameContaining(String materialName);

}