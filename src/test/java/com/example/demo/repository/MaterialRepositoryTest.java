package com.example.demo.repository;

import com.example.demo.model.Material;
import com.example.demo.model.enums.Unit;
import com.example.demo.services.MaterialServices;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MaterialRepositoryTest {

    private final com.example.demo.repository.MaterialRepository repository =
            Mockito.mock(com.example.demo.repository.MaterialRepository.class);

    private final MaterialServices materialService =
            new MaterialServices(repository);

    @Test
    void shouldConvertKilogramToGram() {

        Material material = new Material();
        material.setName("Flour");
        material.setQuantity(10);
        material.setUnit(Unit.KILOGRAM);

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Material saved = materialService.create(material);

        assertThat(saved.getQuantity()).isEqualTo(10_000);
        assertThat(saved.getUnit()).isEqualTo(Unit.GRAM);
    }

    @Test
    void shouldConvertLiterToMilliliter() {

        Material material = new Material();
        material.setName("Milk");
        material.setQuantity(2);
        material.setUnit(Unit.LITER);

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Material saved = materialService.create(material);

        assertThat(saved.getQuantity()).isEqualTo(2_000);
        assertThat(saved.getUnit()).isEqualTo(Unit.MILLILITER);
    }

    @Test
    void shouldKeepBaseUnitUnchanged() {

        Material material = new Material();
        material.setName("Sugar");
        material.setQuantity(500);
        material.setUnit(Unit.GRAM);

        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Material saved = materialService.create(material);

        assertThat(saved.getQuantity()).isEqualTo(500);
        assertThat(saved.getUnit()).isEqualTo(Unit.GRAM);
    }
}