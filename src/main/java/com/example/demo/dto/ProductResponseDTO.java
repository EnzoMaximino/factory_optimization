package com.example.demo.dto;

import com.example.demo.model.Product;
import com.example.demo.model.ProductMaterial;

import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        Double value,
        List<CompositionResponseDTO> composition
) {

    public static ProductResponseDTO fromEntity(Product product) {

        List<CompositionResponseDTO> compositionList =
                product.getComposition()
                        .stream()
                        .map(ProductResponseDTO::mapComposition)
                        .toList();

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getValue(),
                compositionList
        );
    }

    private static CompositionResponseDTO mapComposition(ProductMaterial pm) {
        return new CompositionResponseDTO(
                pm.getMaterial().getId(),
                pm.getMaterial().getName(),
                pm.getQuantity()
        );
    }
}