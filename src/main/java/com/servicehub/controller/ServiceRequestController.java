package com.servicehub.controller;

import com.servicehub.dto.ServiceRequestDTO;
import com.servicehub.dto.ServiceRequestResponseDTO;
import com.servicehub.dto.ServiceRequestStatusDTO;
import com.servicehub.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(ServiceRequestService serviceRequestService) {
        this.serviceRequestService = serviceRequestService;
    }

    @PostMapping
    public ServiceRequestResponseDTO saveRequest(
            @Valid @RequestBody ServiceRequestDTO serviceRequestDTO) {
        return ServiceRequestResponseDTO.from(
                serviceRequestService.saveRequest(serviceRequestDTO));
    }

    @GetMapping("/{id}")
    public ServiceRequestResponseDTO getRequestById(@PathVariable Long id) {
        return ServiceRequestResponseDTO.from(
                serviceRequestService.getRequestById(id));
    }

    @PutMapping("/{id}")
    public ServiceRequestResponseDTO updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequestDTO serviceRequestDTO) {
        return ServiceRequestResponseDTO.from(
                serviceRequestService.updateRequest(id, serviceRequestDTO));
    }

    @PutMapping("/{id}/status")
    public ServiceRequestResponseDTO updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequestStatusDTO statusDTO) {
        return ServiceRequestResponseDTO.from(
                serviceRequestService.updateStatus(id, statusDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id) {
        serviceRequestService.deleteRequest(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<ServiceRequestResponseDTO> getRequestsByCustomer(
            @PathVariable Long customerId) {
        return serviceRequestService.getRequestsByCustomer(customerId).stream()
                .map(ServiceRequestResponseDTO::from)
                .toList();
    }

    @GetMapping("/provider/{providerId}")
    public List<ServiceRequestResponseDTO> getRequestsByProvider(
            @PathVariable Long providerId) {
        return serviceRequestService.getRequestByProvider(providerId).stream()
                .map(ServiceRequestResponseDTO::from)
                .toList();
    }
}