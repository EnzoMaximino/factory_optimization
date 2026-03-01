package com.example.demo.dto;

public record ProductionSuggestionDTO(
        Long productId,
        String productName,
        Double unitValue,
        int maxUnits,
        double totalValue
) {
    public boolean canBeProduced() {
        return maxUnits > 0;
    }
}