package com.servicehub.service;

import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.repository.ServiceCategoryRepository;
import org.springframework.stereotype.Service;
import com.servicehub.dto.ServiceCategoryDTO;
import com.servicehub.entity.ServiceCategory;
import java.util.List;
import java.util.Optional;
import com.servicehub.exception.CategoryNotFoundException;

@Service
public class ServiceCategoryService {

    private final ServiceCategoryRepository serviceCategoryRepository;

    public ServiceCategoryService(ServiceCategoryRepository serviceCategoryRepository){

        this.serviceCategoryRepository = serviceCategoryRepository;

    }
    public ServiceCategory saveCategory(ServiceCategoryDTO serviceCategoryDTO){

        ServiceCategory serviceCategory = new ServiceCategory();

        serviceCategory.setCategoryName(serviceCategoryDTO.getCategoryName());

        return serviceCategoryRepository.save(serviceCategory);
    }
    public List<ServiceCategory> getAllCategories() {
        return serviceCategoryRepository.findAll();
    }
    public ServiceCategory getCategoryById(Long id){

        return serviceCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Category with id " + id + " not found"));
    }
    public ServiceCategory updateCategory(Long id,
                                          ServiceCategoryDTO serviceCategoryDTO){
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id)
                .orElseThrow(()->
                        new CustomerNotFoundException("Category with id " + id + " not found"));

        serviceCategory.setCategoryName(serviceCategoryDTO.getCategoryName());

        return serviceCategoryRepository.save(serviceCategory);

    }
    public void deleteCategory(Long id){
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Category with id " + id + " not found"));

        serviceCategoryRepository.delete(serviceCategory);
    }
}

