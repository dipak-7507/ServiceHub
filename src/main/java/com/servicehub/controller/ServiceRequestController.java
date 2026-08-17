package com.servicehub.controller;

import com.servicehub.dto.ServiceRequestDTO;
import com.servicehub.entity.ServiceRequest;
import com.servicehub.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.servicehub.dto.ServiceRequestStatusDTO;

import java.util.List;
import java.lang.Long;

@RestController
@RequestMapping("/requests")
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    public ServiceRequestController(
    ServiceRequestService serviceRequestService){
        this.serviceRequestService = serviceRequestService;
    }
    @PostMapping
    public ServiceRequest saveRequest(
            @Valid @RequestBody ServiceRequestDTO serviceRequestDTO ){

        return serviceRequestService.saveRequest(serviceRequestDTO);

    }
    @GetMapping
    public List<ServiceRequest>getAllRequest(){

        return serviceRequestService.getAllRequests();
    }

    @GetMapping("/{id}")

    public ServiceRequest getRequestById(@PathVariable Long id){

        return serviceRequestService.getRequestById(id);

    }

    @PutMapping("/{id}")
    public ServiceRequest updateRequest(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequestDTO serviceRequestDTO){

        return serviceRequestService.updateRequest(id,serviceRequestDTO);
    }


     @PutMapping("/{id}/status")
    public ServiceRequest updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ServiceRequestStatusDTO statusDTO){

        return serviceRequestService.updateStatus(id,statusDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRequest(@PathVariable Long id){

        serviceRequestService.deleteRequest(id);
    }
    @GetMapping("/customer/{customerId}")
    public List<ServiceRequest> getRequestsByCustomer(
            @PathVariable Long customerId){

        return serviceRequestService
                .getRequestsByCustomer(customerId);
    }
    @GetMapping("/provider/{providerId}")
    public List<ServiceRequest> getRequestsByProvider(
            @PathVariable Long providerId){

        return serviceRequestService
                .getRequestByProvider(providerId);
    }
}
