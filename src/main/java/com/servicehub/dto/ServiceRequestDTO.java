package com.servicehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServiceRequestDTO {
    @NotBlank(message = "Description is Required")
    private String description;

    @NotNull(message = "Customer Id is Required")
    private Long customerId;

    @NotNull(message = "Provider Id is Required")
    private Long providerId;


    public ServiceRequestDTO(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }
}
