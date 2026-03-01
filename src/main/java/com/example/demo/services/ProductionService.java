package com.example.demo.services;

import com.example.demo.dto.ProductionPlanDTO;
import com.example.demo.dto.ProductionSuggestionDTO;
import com.example.demo.model.Material;
import com.example.demo.model.Product;
import com.example.demo.model.ProductMaterial;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    public List<ProductionPlanDTO> calculateBestProductionPlan() {

        Map<Long, Integer> stock = loadMaterialStock();
        List<Product> remainingProducts =
                new ArrayList<>(productRepository.findAll());

        List<ProductionPlanDTO> result = new ArrayList<>();

        while (!remainingProducts.isEmpty()) {

            Product bestProduct = null;
            int bestUnits = 0;
            double bestValue = 0;

            for (Product product : remainingProducts) {

                int maxUnits = calculateMaxUnits(product, stock);

                double potentialValue = maxUnits * product.getValue();

                if (potentialValue > bestValue) {
                    bestValue = potentialValue;
                    bestUnits = maxUnits;
                    bestProduct = product;
                }
            }

            if (bestProduct == null || bestUnits == 0) break;

            consumeStock(bestProduct, stock, bestUnits);

            result.add(new ProductionPlanDTO(
                    bestProduct.getId(),
                    bestProduct.getName(),
                    bestProduct.getValue(),
                    bestUnits,
                    bestUnits * bestProduct.getValue()
            ));

            remainingProducts.remove(bestProduct);
        }

        return result;
    }

    private Map<Long, Integer> loadMaterialStock() {
        Map<Long, Integer> stock = new HashMap<>();

        for (Material material : materialRepository.findAll()) {
            stock.put(material.getId(), material.getQuantity());
        }

        return stock;
    }

    private int calculateMaxUnits(Product product,
                                  Map<Long, Integer> stock) {

        int maxUnits = Integer.MAX_VALUE;

        for (ProductMaterial pm : product.getComposition()) {

            int available = stock.getOrDefault(
                    pm.getMaterial().getId(),
                    0
            );

            int possible = available / pm.getQuantity();

            maxUnits = Math.min(maxUnits, possible);

            if (maxUnits == 0) break;
        }

        return maxUnits == Integer.MAX_VALUE ? 0 : maxUnits;
    }

    private void consumeStock(Product product,
                              Map<Long, Integer> stock,
                              int units) {

        for (ProductMaterial pm : product.getComposition()) {

            Long materialId = pm.getMaterial().getId();

            int consumed = pm.getQuantity() * units;

            stock.put(
                    materialId,
                    stock.get(materialId) - consumed
            );
        }
    }
}