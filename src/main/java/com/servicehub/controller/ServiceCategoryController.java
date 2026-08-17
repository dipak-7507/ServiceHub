package com.servicehub.controller;

import com.servicehub.dto.ServiceCategoryDTO;
import com.servicehub.entity.ServiceCategory;
import com.servicehub.service.ServiceCategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class ServiceCategoryController {

    private final ServiceCategoryService serviceCategoryService;

    public ServiceCategoryController(ServiceCategoryService serviceCategoryService){

        this.serviceCategoryService = serviceCategoryService;


    }
    @PostMapping
    public ServiceCategory saveCategory(

            @Valid @RequestBody ServiceCategoryDTO serviceCategoryDTO){

        return serviceCategoryService.saveCategory(serviceCategoryDTO);
    }
    @GetMapping
    public List<ServiceCategory> getAllCategories() {
        return serviceCategoryService.getAllCategories();
    }
    @GetMapping("/{id}")
    public ServiceCategory getCategoryById(@PathVariable Long id){

        return serviceCategoryService.getCategoryById(id);
    }
    @PutMapping ("/{id}")
    public ServiceCategory updateCategory(
        @PathVariable Long id,
                @Valid @RequestBody ServiceCategoryDTO serviceCategoryDTO){

        return serviceCategoryService.updateCategory(id , serviceCategoryDTO);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id){
        serviceCategoryService.deleteCategory(id);
    }
}
