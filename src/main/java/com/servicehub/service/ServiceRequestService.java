package com.servicehub.service;

import com.servicehub.dto.ServiceRequestDTO;
import com.servicehub.dto.ServiceRequestStatusDTO;
import com.servicehub.entity.Customer;
import com.servicehub.entity.RequestStatus;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.entity.ServiceRequest;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.InvalidRequestException;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.ResourceNotFoundException;
import com.servicehub.exception.UnauthorizedAccessException;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.ServiceProviderRepository;
import com.servicehub.repository.ServiceRequestRepository;
import com.servicehub.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A ServiceRequest is an ENQUIRY: the customer describes the problem
 * ("my tap is leaking") and the provider accepts or rejects it.
 * A Booking is the scheduled job with a date. (Enquiry first, booking later.)
 */
@Service
public class ServiceRequestService {

    private final ServiceRequestRepository serviceRequestRepository;
    private final CustomerRepository customerRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    public ServiceRequestService(
            ServiceRequestRepository serviceRequestRepository,
            CustomerRepository customerRepository,
            ServiceProviderRepository serviceProviderRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
        this.customerRepository = customerRepository;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    // ---------- helpers ----------

    private boolean isRequestCustomer(ServiceRequest r) {
        return SecurityUtil.isLoggedInCustomer(r.getCustomer().getEmail());
    }

    private boolean isRequestProvider(ServiceRequest r) {
        return SecurityUtil.isLoggedInProvider(r.getServiceProvider().getEmail());
    }

    private void checkAccess(ServiceRequest r) {
        if (!isRequestCustomer(r) && !isRequestProvider(r)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this service request");
        }
    }

    private ServiceRequest findRequest(Long id) {
        return serviceRequestRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Service request not found"));
    }

    // ---------- use cases ----------

    @Transactional
    public ServiceRequest saveRequest(ServiceRequestDTO dto) {
        Customer customer = customerRepository
                .findById(dto.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!SecurityUtil.isLoggedInCustomer(customer.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You can only create requests for yourself");
        }

        ServiceProvider provider = serviceProviderRepository
                .findById(dto.getProviderId())
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        ServiceRequest request = new ServiceRequest();
        request.setDescription(dto.getDescription());
        request.setCustomer(customer);
        request.setServiceProvider(provider);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus(RequestStatus.PENDING);

        return serviceRequestRepository.save(request);
    }

    public ServiceRequest getRequestById(Long id) {
        ServiceRequest request = findRequest(id);
        checkAccess(request);
        return request;
    }

    /** Only the description can be edited, only by the customer, only while PENDING. */
    @Transactional
    public ServiceRequest updateRequest(Long id, ServiceRequestDTO dto) {
        ServiceRequest request = findRequest(id);

        if (!isRequestCustomer(request)) {
            throw new UnauthorizedAccessException(
                    "Only the customer can edit this request");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new InvalidRequestException(
                    "Only a PENDING request can be edited");
        }

        request.setDescription(dto.getDescription());
        return serviceRequestRepository.save(request);
    }

    @Transactional
    public ServiceRequest updateStatus(Long id, ServiceRequestStatusDTO statusDTO) {
        ServiceRequest request = findRequest(id);
        checkAccess(request);

        RequestStatus current = request.getStatus();
        RequestStatus next = statusDTO.getStatus();

        if (isRequestProvider(request)) {
            if (!current.providerCanMoveTo(next)) {
                throw new InvalidRequestException(
                        "Cannot change request from " + current + " to " + next);
            }
        } else {
            if (next != RequestStatus.CANCELLED) {
                throw new UnauthorizedAccessException(
                        "Customers can only cancel a request");
            }
            if (!current.customerCanMoveTo(next)) {
                throw new InvalidRequestException(
                        "Cannot change request from " + current + " to " + next);
            }
        }

        request.setStatus(next);
        return serviceRequestRepository.save(request);
    }

    @Transactional
    public void deleteRequest(Long id) {
        ServiceRequest request = findRequest(id);

        if (!isRequestCustomer(request)) {
            throw new UnauthorizedAccessException(
                    "Only the customer can delete this request");
        }
        serviceRequestRepository.delete(request);
    }

    public List<ServiceRequest> getRequestsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!SecurityUtil.isLoggedInCustomer(customer.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to see this customer's requests");
        }
        return serviceRequestRepository.findByCustomerId(customerId);
    }

    public List<ServiceRequest> getRequestByProvider(Long providerId) {
        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        if (!SecurityUtil.isLoggedInProvider(provider.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to see this provider's requests");
        }
        return serviceRequestRepository.findByServiceProviderId(providerId);
    }
}