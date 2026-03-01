package com.example.demo.services;

import com.example.demo.dto.ProductCreateDTO;
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
public class ProductsServices{

    private final ProductRepository repository;
    private final MaterialRepository materialRepository;

    public Product create(ProductCreateDTO dto) {

        Product product = new Product();
        product.setName(dto.name());
        product.setValue(dto.value());

        List<ProductMaterial> composition = dto.composition()
                .stream()
                .map(c -> {

                    Material material = materialRepository
                            .findById(c.materialId())
                            .orElseThrow(() ->
                                    new RuntimeException("Material not found"));

                    ProductMaterial pm = new ProductMaterial();
                    pm.setProduct(product);
                    pm.setMaterial(material);
                    pm.setQuantity(c.quantity());

                    return pm;
                })
                .toList();

        product.setComposition(composition);

        return repository.save(product);
    }

    public Product update(Long id, Product updated) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(updated.getName());
        existing.setValue(updated.getValue());
        existing.setComposition(updated.getComposition());

        return repository.save(existing);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}