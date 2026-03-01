package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductResponseDTO toResponse(Product product) {

        List<CompositionResponseDTO> composition =
                product.getComposition()
                        .stream()
                        .map(this::toCompositionResponse)
                        .toList();

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getValue(),
                composition
        );
    }

    private CompositionResponseDTO toCompositionResponse(ProductMaterial pm) {

        return new CompositionResponseDTO(
                pm.getMaterial().getId(),
                pm.getMaterial().getName(),
                pm.getQuantity()
        );
    }
}