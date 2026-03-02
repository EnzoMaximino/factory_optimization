package com.example.demo.services;

import com.example.demo.dto.ProductionPlanDTO;
import com.example.demo.model.Material;
import com.example.demo.model.Product;
import com.example.demo.model.ProductMaterial;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    public List<ProductionPlanDTO> calculateBestProductionPlan() {

        Map<Long, Double> stock = loadMaterialStock();
        List<Product> products = productRepository.findAll();

        Map<Product, Integer> productionCount = new HashMap<>();

        while (true) {

            Product bestProduct = null;
            double bestEfficiency = 0;

            for (Product product : products) {

                if (!canProduce(product, stock)) continue;

                double efficiency = calculateEfficiency(product, stock);

                if (efficiency > bestEfficiency) {
                    bestEfficiency = efficiency;
                    bestProduct = product;
                }
            }

            if (bestProduct == null) break;

            consumeStock(bestProduct, stock, 1);

            productionCount.put(
                    bestProduct,
                    productionCount.getOrDefault(bestProduct, 0) + 1
            );
        }

        return buildResult(productionCount);
    }

    private Map<Long, Double> loadMaterialStock() {
        Map<Long, Double> stock = new HashMap<>();
        for (Material material : materialRepository.findAll()) {
            stock.put(material.getId(), material.getQuantity());
        }
        return stock;
    }

    private boolean canProduce(Product product, Map<Long, Double> stock) {

        for (ProductMaterial pm : product.getComposition()) {

            double available = stock.getOrDefault(
                    pm.getMaterial().getId(),
                    0.0
            );

            if (available < pm.getQuantity()) {
                return false;
            }
        }

        return true;
    }

    private double calculateEfficiency(Product product,
                                       Map<Long, Double> stock) {

        double maxImpact = 0;

        for (ProductMaterial pm : product.getComposition()) {

            double totalStock = stock.getOrDefault(
                    pm.getMaterial().getId(),
                    0.0
            );

            if (totalStock == 0) return 0;

            double impact = pm.getQuantity() / totalStock;

            maxImpact = Math.max(maxImpact, impact);
        }

        if (maxImpact == 0) return 0;

        return product.getValue() / maxImpact;
    }

    private void consumeStock(Product product,
                              Map<Long, Double> stock,
                              int units) {

        for (ProductMaterial pm : product.getComposition()) {

            Long materialId = pm.getMaterial().getId();
            double consumed = pm.getQuantity() * units;

            stock.put(
                    materialId,
                    stock.get(materialId) - consumed
            );
        }
    }

    private List<ProductionPlanDTO> buildResult(
            Map<Product, Integer> productionCount) {

        List<ProductionPlanDTO> result = new ArrayList<>();

        for (Map.Entry<Product, Integer> entry : productionCount.entrySet()) {

            Product product = entry.getKey();
            int units = entry.getValue();

            result.add(new ProductionPlanDTO(
                    product.getId(),
                    product.getName(),
                    product.getValue(),
                    units,
                    units * product.getValue()
            ));
        }

        result.sort(Comparator.comparing(ProductionPlanDTO::totalValue)
                .reversed());

        return result;
    }

    @Transactional
    public List<ProductionPlanDTO> executeBestProductionPlan() {

        List<ProductionPlanDTO> plan = calculateBestProductionPlan();

        for (ProductionPlanDTO item : plan) {

            Product product = productRepository
                    .findById(item.productId())
                    .orElseThrow();

            int units = item.unitsProduced();

            for (ProductMaterial pm : product.getComposition()) {

                Material material = pm.getMaterial();

                double consumed = pm.getQuantity() * units;

                material.setQuantity(
                        material.getQuantity() - consumed
                );

                materialRepository.save(material);
            }
        }

        return plan;
    }
}