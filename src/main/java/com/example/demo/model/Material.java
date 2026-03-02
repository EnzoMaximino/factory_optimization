package com.example.demo.model;

import com.example.demo.model.enums.Unit;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    private String name;

    @Column(nullable = false)
    @Min(0)
    private double quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;
}