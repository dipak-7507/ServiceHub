package com.servicehub.dto;

import jakarta.validation.constraints.NotBlank;
public class ServiceCategoryDTO {
    @NotBlank(message = "Category Name is Required")

    private  String categoryName;

    public ServiceCategoryDTO(){
    }
    public ServiceCategoryDTO(String categoryName){
        this.categoryName = categoryName;

    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
