package com.servicehub.dto;

import jakarta.validation.constraints.NotBlank;

public class ServiceRequestStatusDTO {

    @NotBlank(message = "Status is required")

    private String status;

    public ServiceRequestStatusDTO(){

    }
    public String getStatus(){
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
