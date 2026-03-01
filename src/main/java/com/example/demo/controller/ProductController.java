package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.services.ProductsServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductsServices service;
    private final ProductMapper mapper;

    @PostMapping
    public ProductResponseDTO create(
            @Valid @RequestBody ProductCreateDTO dto) {

        return mapper.toResponse(service.create(dto));
    }
    
    @GetMapping
    public Page<ProductResponseDTO> findAll(Pageable pageable) {

        return service.findAll(pageable)
                .map(mapper::toResponse);
    }
}