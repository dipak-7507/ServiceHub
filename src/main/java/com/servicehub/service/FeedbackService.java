package com.servicehub.service;

import com.servicehub.dto.FeedbackDTO;
import com.servicehub.entity.BookingStatus;
import com.servicehub.entity.Customer;
import com.servicehub.entity.Feedback;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.exception.ConflictException;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.InvalidRequestException;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.ResourceNotFoundException;
import com.servicehub.exception.UnauthorizedAccessException;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.FeedbackRepository;
import com.servicehub.repository.ServiceProviderRepository;
import com.servicehub.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final CustomerRepository customerRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final BookingRepository bookingRepository;

    public FeedbackService(
            FeedbackRepository feedbackRepository,
            CustomerRepository customerRepository,
            ServiceProviderRepository serviceProviderRepository,
            BookingRepository bookingRepository) {
        this.feedbackRepository = feedbackRepository;
        this.customerRepository = customerRepository;
        this.serviceProviderRepository = serviceProviderRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public Feedback saveFeedback(FeedbackDTO feedbackDTO) {

        Customer customer = customerRepository
                .findById(feedbackDTO.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        // feedback can only be given as yourself
        if (!SecurityUtil.isLoggedInCustomer(customer.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You can only give feedback as yourself");
        }

        ServiceProvider provider = serviceProviderRepository
                .findById(feedbackDTO.getProviderId())
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        // rule 1: only customers who really received the service can rate it
        boolean hasCompletedBooking = bookingRepository
                .existsByCustomerIdAndServiceProviderIdAndStatus(
                        feedbackDTO.getCustomerId(),
                        feedbackDTO.getProviderId(),
                        BookingStatus.COMPLETED);
        if (!hasCompletedBooking) {
            throw new InvalidRequestException(
                    "You can give feedback only after a completed booking with this provider");
        }

        // rule 2: one feedback per customer per provider (no rating spam)
        if (feedbackRepository.existsByCustomerIdAndServiceProviderId(
                feedbackDTO.getCustomerId(), feedbackDTO.getProviderId())) {
            throw new ConflictException(
                    "You have already given feedback to this provider");
        }

        Feedback feedback = new Feedback();
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setCustomer(customer);
        feedback.setServiceProvider(provider);

        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbacksByProvider(Long providerId) {
        if (!serviceProviderRepository.existsById(providerId)) {
            throw new ProviderNotFoundException("Provider not found");
        }
        return feedbackRepository.findByServiceProviderId(providerId);
    }

    public Double getAverageRatingByProvider(Long providerId) {
        if (!serviceProviderRepository.existsById(providerId)) {
            throw new ProviderNotFoundException("Provider not found");
        }
        Double average = feedbackRepository.getAverageRatingByProviderId(providerId);
        return average == null ? 0.0 : average;
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Feedback not found"));
    }

    @Transactional
    public void deleteFeedback(Long id) {
        Feedback feedback = getFeedbackById(id);

        // only the author can delete his own feedback
        if (!SecurityUtil.isLoggedInCustomer(feedback.getCustomer().getEmail())) {
            throw new UnauthorizedAccessException(
                    "You can delete only your own feedback");
        }
        feedbackRepository.delete(feedback);
    }

    public List<Feedback> getFeedbackByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!SecurityUtil.isLoggedInCustomer(customer.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You can see only your own feedback list");
        }
        return feedbackRepository.findByCustomerId(customerId);
    }
}