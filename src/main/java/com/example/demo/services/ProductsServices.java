package com.example.demo.services;

import com.example.demo.dto.CompositionDTO;
import com.example.demo.dto.ProductCreateDTO;
import com.example.demo.dto.ProductResponseDTO;
import com.example.demo.model.Material;
import com.example.demo.model.Product;
import com.example.demo.model.ProductMaterial;
import com.example.demo.repository.MaterialRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsServices {

    private final ProductRepository repository;
    private final MaterialRepository materialRepository;

    public Product create(ProductCreateDTO dto) {

        if (repository.existsByName(dto.name())) {
            throw new RuntimeException("Product name already exists");
        }

        Product product = new Product();
        product.setName(dto.name());
        product.setValue(dto.value());

        for (CompositionDTO item : dto.composition()) {

            Material material = materialRepository
                    .findById(item.materialId())
                    .orElseThrow(() ->
                            new RuntimeException("Material not found"));

            ProductMaterial pm = new ProductMaterial();
            pm.setProduct(product);
            pm.setMaterial(material);
            pm.setQuantity(item.quantity());

            product.getComposition().add(pm);
        }

        return repository.save(product);
    }


    public Product update(Long id, ProductCreateDTO dto) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (repository.existsByNameAndIdNot(dto.name(), id)) {
            throw new RuntimeException("Product name already exists");
        }

        product.setName(dto.name());
        product.setValue(dto.value());

        product.getComposition().clear();


        repository.flush();

        for (CompositionDTO item : dto.composition()) {

            Material material = materialRepository.findById(item.materialId())
                    .orElseThrow(() -> new RuntimeException("Material not found"));

            ProductMaterial pm = new ProductMaterial();
            pm.setProduct(product);
            pm.setMaterial(material);
            pm.setQuantity(item.quantity());

            product.getComposition().add(pm);
        }

        return repository.save(product);
    }

    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void delete(Long id) {

        Product product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        repository.delete(product);
    }

    public List<ProductResponseDTO> filterByMaterial(String materialName) {

        List<Product> products =
                repository.findByMaterialNameContaining(materialName);

        return products.stream()
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }
}