package com.servicehub.service;

import com.servicehub.dto.DashboardDTO;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final BookingRepository bookingRepository;

    private final FeedbackRepository feedbackRepository;

    public DashboardService(
            BookingRepository  bookingRepository,
            FeedbackRepository feedbackRepository){

        this.bookingRepository = bookingRepository;
        this.feedbackRepository = feedbackRepository;

    }

    public DashboardDTO getProviderDashboard(Long providerId){

        DashboardDTO dashboard = new DashboardDTO();

        dashboard.setTotalBookings(
                bookingRepository
                        .countByServiceProviderId(providerId));

                dashboard.setCompleteBookings(
                        bookingRepository
                                .countByServiceProviderIdAndStatus(
                                providerId,"COMPLETED"));

                dashboard.setPendingBookings(bookingRepository
                        .countByServiceProviderIdAndStatus(
                                providerId,"PENDING"));

                dashboard.setTotalFeedback(feedbackRepository
                        .countByServiceProviderId(providerId));

                dashboard.setAverageRating(
                        feedbackRepository
                                .getAverageRatingByProviderId(providerId));

                return dashboard;
    }
}
