package com.example.demo.dto;

public record ProductionPlanDTO(
        Long productId,
        String productName,
        Double unitValue,
        Integer unitsProduced,
        Double totalValue
) {}
