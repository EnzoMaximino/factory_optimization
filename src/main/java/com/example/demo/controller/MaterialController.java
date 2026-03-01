package com.example.demo.controller;

import com.example.demo.model.Material;
import com.example.demo.services.MaterialServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialServices materialServices;

    @PostMapping
    public Material create(@RequestBody Material material) {
        return materialServices.create(material);
    }

    @GetMapping
    public List<Material> findAll() {
        return materialServices.findAll();
    }

    @PutMapping("/{id}")
    public Material update(@PathVariable Long id,
                           @RequestBody Material material) {
        return materialServices.update(id, material);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        materialServices.delete(id);
    }
}