package com.servicehub.service;

import com.servicehub.dto.FeedbackDTO;
import com.servicehub.entity.Customer;
import com.servicehub.entity.Feedback;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.ResourceNotFoundException;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.FeedbackRepository;
import com.servicehub.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class FeedbackService {
private final FeedbackRepository feedbackRepository;
private final CustomerRepository customerRepository;
private final ServiceProviderRepository serviceProviderRepository;

public FeedbackService(
        FeedbackRepository feedbackRepository,
        CustomerRepository customerRepository,
        ServiceProviderRepository serviceProviderRepository){
    this.feedbackRepository = feedbackRepository;
    this.customerRepository = customerRepository;
    this.serviceProviderRepository = serviceProviderRepository;
}
public Feedback saveFeedback(FeedbackDTO feedbackDTO){

    Customer customer = customerRepository
            .findById(feedbackDTO.getCustomerId())
            .orElseThrow(()->
                    new CustomerNotFoundException("Customer not found"));

    ServiceProvider provider = serviceProviderRepository
            .findById(feedbackDTO.getProviderId())
            .orElseThrow(()->
                    new ProviderNotFoundException("Provider not found"));

    Feedback feedback = new Feedback();
    feedback.setRating(feedbackDTO.getRating());
    feedback.setComment(feedbackDTO.getComment());
    feedback.setCreatedAt(LocalDateTime.now());
    feedback.setCustomer(customer);
    feedback.setServiceProvider(provider);

    return feedbackRepository.save(feedback);
 }
    public List<Feedback> getAllFeedbacks(){

        return feedbackRepository.findAll();
    }

    public List<Feedback> getFeedbacksByProvider(
            Long providerId){

        return feedbackRepository
                .findByServiceProviderId(providerId);
    }
    public Double getAverageRatingByProvider(Long providerId){

    return feedbackRepository
            .getAverageRatingByProviderId(providerId);
    }
    public Feedback getFeedbackById(Long id){

    return feedbackRepository.findById(id)
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Feedback not found"));
 }

 public void deleteFeedback(Long id){

    Feedback feedback = feedbackRepository.findById(id)
            .orElseThrow(()->
                   new ResourceNotFoundException("Feedback not found"));

    feedbackRepository.delete(feedback);

 }

 public List<Feedback> getFeedbackByCustomer(Long customerId){

    return feedbackRepository.findByCustomerId(customerId);
 }

}
