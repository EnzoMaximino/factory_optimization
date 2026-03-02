package com.example.demo.controller;

import com.example.demo.dto.ProductionPlanDTO;
import com.example.demo.dto.ProductionSuggestionDTO;
import com.example.demo.services.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/production")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionController {

    private final ProductionService productionService;

    @GetMapping("/plan")
    public List<ProductionPlanDTO> getBestPlan() {
        return productionService.calculateBestProductionPlan();
    }

    @PostMapping("/execute")
    public List<ProductionPlanDTO> executePlan() {
        return productionService.executeBestProductionPlan();
    }

}