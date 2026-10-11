package com.servicehub.dto;

import com.servicehub.entity.RequestStatus;
import com.servicehub.entity.ServiceRequest;

import java.time.LocalDateTime;

public record ServiceRequestResponseDTO(
        Long id,
        String description,
        LocalDateTime requestDate,
        RequestStatus status,
        Long customerId,
        String customerName,
        Long providerId,
        String providerName) {

    public static ServiceRequestResponseDTO from(ServiceRequest r) {
        return new ServiceRequestResponseDTO(
                r.getId(),
                r.getDescription(),
                r.getRequestDate(),
                r.getStatus(),
                r.getCustomer().getId(),
                r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName(),
                r.getServiceProvider().getId(),
                r.getServiceProvider().getFirstName() + " " + r.getServiceProvider().getLastName());
    }
}