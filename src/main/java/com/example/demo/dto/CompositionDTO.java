package com.example.demo.dto;

import jakarta.validation.constraints.*;

public record CompositionDTO(

        @NotNull(message = "Material ID is required")
        Long materialId,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        double quantity
) {}