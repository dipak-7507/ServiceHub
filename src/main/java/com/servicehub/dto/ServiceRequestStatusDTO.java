package com.servicehub.dto;

import com.servicehub.entity.RequestStatus;
import jakarta.validation.constraints.NotNull;

public class ServiceRequestStatusDTO {

    @NotNull(message = "Status is required")
    private RequestStatus status;

    public ServiceRequestStatusDTO() {
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}