package com.example.demo.services;

import com.example.demo.model.Material;
import com.example.demo.model.enums.Unit;
import com.example.demo.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServices{

    private final MaterialRepository repository;

    public Material create(Material material) {
        normalize(material);
        return repository.save(material);
    }

    public Material update(Long id, Material updatedMaterial) {

        Material existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material not found"));

        existing.setName(updatedMaterial.getName());
        existing.setQuantity(updatedMaterial.getQuantity());
        existing.setUnit(updatedMaterial.getUnit());

        normalize(existing);

        return repository.save(existing);
    }

    public List<Material> findAll() {
        return repository.findAll();
    }

    private void normalize(Material material) {

        Unit unit = material.getUnit();

        int normalizedQuantity = unit.toBase(material.getQuantity());

        material.setQuantity(normalizedQuantity);
        material.setUnit(unit.getBaseUnit());
    }
    public void delete(Long id) {
        repository.deleteById(id);
    }
}