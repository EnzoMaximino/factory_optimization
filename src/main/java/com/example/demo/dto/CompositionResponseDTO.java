package com.example.demo.dto;

public record CompositionResponseDTO(
        Long materialId,
        String materialName,
        Integer quantity
) {}