package com.servicehub.service;

import com.servicehub.entity.Customer;
import com.servicehub.dto.ServiceRequestDTO;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.entity.ServiceRequest;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.ServiceRequestRepository;
import org.springframework.stereotype.Service;
import com.servicehub.dto.ServiceRequestStatusDTO;
import com.servicehub.exception.ResourceNotFoundException;


import java.time.LocalDateTime;
import java.util.List;


@Service
public class ServiceRequestService {
    private final ServiceRequestRepository serviceRequestRepository;

    private final CustomerRepository customerRepository;

    private final ServiceProviderService serviceProviderService;

    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            CustomerRepository customerRepository,
            ServiceProviderService serviceProviderService){

        this.serviceRequestRepository = serviceRequestRepository;
        this.customerRepository = customerRepository;
        this.serviceProviderService = serviceProviderService;

    }
    public ServiceRequest saveRequest(ServiceRequestDTO serviceRequestDTO){
        Customer customer = customerRepository
                .findById(serviceRequestDTO.getCustomerId())
                .orElseThrow(()->
                        new CustomerNotFoundException("Customer not found"));

        ServiceProvider serviceProvider =
                serviceProviderService.getProviderById(
                        serviceRequestDTO.getProviderId());

        ServiceRequest serviceRequest = new ServiceRequest();

        serviceRequest.setDescription(serviceRequestDTO.getDescription());

        serviceRequest.setCustomer(customer);

        serviceRequest.setServiceProvider(serviceProvider);

        serviceRequest.setRequestDate(LocalDateTime.now());

        serviceRequest.setStatus("PENDING");

        return serviceRequestRepository.save(serviceRequest);
    }
    public List<ServiceRequest>getAllRequests() {

        return serviceRequestRepository.findAll();
    }

    public ServiceRequest getRequestById(Long id){

        return serviceRequestRepository.findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Service request not found"));
    }
    public ServiceRequest updateStatus(
            Long id,
            ServiceRequestStatusDTO statusDTO) {

        ServiceRequest serviceRequest = serviceRequestRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service request not found"));

        serviceRequest.setStatus(statusDTO.getStatus());

        return serviceRequestRepository.save(serviceRequest);
    }

    public void deleteRequest(Long id){
        ServiceRequest serviceRequest = serviceRequestRepository
                .findById(id)
                .orElseThrow(()->
                        new ResourceNotFoundException("Service request not found"));

        serviceRequestRepository.delete(serviceRequest);

    }
    public ServiceRequest updateRequest(
            Long id,
            ServiceRequestDTO serviceRequestDTO){
        ServiceRequest serviceRequest =
                serviceRequestRepository.findById(id)
                        .orElseThrow(()->
                                new ResourceNotFoundException("Service request not found"));

        Customer customer =
                customerRepository.findById(
                        serviceRequestDTO.getCustomerId())
                        .orElseThrow(()->
                                new CustomerNotFoundException("Customer not found"));

        ServiceProvider serviceProvider =
                serviceProviderService.getProviderById(
                        serviceRequestDTO.getProviderId());

        serviceRequest.setDescription(
                serviceRequestDTO.getDescription());
        serviceRequest.setCustomer(customer);

        serviceRequest.setServiceProvider(serviceProvider);

        return serviceRequestRepository.save(serviceRequest);
    }
    public List<ServiceRequest> getRequestsByCustomer(
            Long customerId){

        return serviceRequestRepository
                .findByCustomerId(customerId);
    }
    public List<ServiceRequest>getRequestByProvider(
            Long providerId){
        return serviceRequestRepository
                .findByServiceProviderId(providerId);
    }
}
