package com.example.demo.services;

import com.example.demo.dto.ProductionPlanDTO;
import com.example.demo.dto.ProductionSuggestionDTO;
import com.example.demo.model.Material;
import com.example.demo.model.Product;
import com.example.demo.model.ProductMaterial;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private ProductionService service;

    @Test
    void shouldPrioritizeProductWithHigherEfficiency() {

        Material metal = new Material();
        metal.setId(1L);
        metal.setQuantity(101);

        Product chair = new Product();
        chair.setId(1L);
        chair.setName("Chair");
        chair.setValue(2.0);

        ProductMaterial pmChair = new ProductMaterial();
        pmChair.setProduct(chair);
        pmChair.setMaterial(metal);
        pmChair.setQuantity(1);

        chair.setComposition(List.of(pmChair));

        Product car = new Product();
        car.setId(2L);
        car.setName("Car");
        car.setValue(100.0);

        ProductMaterial pmCar = new ProductMaterial();
        pmCar.setProduct(car);
        pmCar.setMaterial(metal);
        pmCar.setQuantity(100);

        car.setComposition(List.of(pmCar));

        when(materialRepository.findAll())
                .thenReturn(List.of(metal));

        when(productRepository.findAll())
                .thenReturn(List.of(car, chair));

        List<ProductionPlanDTO> result =
                service.calculateBestProductionPlan();

        assertEquals("Chair", result.get(0).productName());
        assertEquals(101, result.get(0).unitsProduced());
    }
}