package com.example.demo.dto;

import java.util.List;

public record ProductResponseDTO(
        Long id,
        String name,
        Double value,
        List<CompositionResponseDTO> composition
) {}