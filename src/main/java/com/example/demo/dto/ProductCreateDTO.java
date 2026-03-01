package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record ProductCreateDTO(

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Value is required")
        @Positive(message = "Value must be positive")
        Double value,

        @NotEmpty(message = "Composition cannot be empty")
        List<CompositionDTO> composition
) {}