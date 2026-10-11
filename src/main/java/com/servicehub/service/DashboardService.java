package com.servicehub.service;

import com.servicehub.dto.DashboardDTO;
import com.servicehub.entity.BookingStatus;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.UnauthorizedAccessException;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.FeedbackRepository;
import com.servicehub.repository.ServiceProviderRepository;
import com.servicehub.security.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final BookingRepository bookingRepository;
    private final FeedbackRepository feedbackRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    public DashboardService(
            BookingRepository bookingRepository,
            FeedbackRepository feedbackRepository,
            ServiceProviderRepository serviceProviderRepository) {

        this.bookingRepository = bookingRepository;
        this.feedbackRepository = feedbackRepository;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    public DashboardDTO getProviderDashboard(Long providerId) {

        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        // a provider can see only his own numbers
        if (!SecurityUtil.isLoggedInProvider(provider.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You can see only your own dashboard");
        }

        DashboardDTO dashboard = new DashboardDTO();

        dashboard.setTotalBookings(
                bookingRepository.countByServiceProviderId(providerId));

        dashboard.setCompleteBookings(
                bookingRepository.countByServiceProviderIdAndStatus(
                        providerId, BookingStatus.COMPLETED));

        dashboard.setPendingBookings(
                bookingRepository.countByServiceProviderIdAndStatus(
                        providerId, BookingStatus.PENDING));

        dashboard.setTotalFeedback(
                feedbackRepository.countByServiceProviderId(providerId));

        Double average = feedbackRepository.getAverageRatingByProviderId(providerId);
        dashboard.setAverageRating(average == null ? 0.0 : average);

        return dashboard;
    }
}