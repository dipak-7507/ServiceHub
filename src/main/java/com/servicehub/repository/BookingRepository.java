package com.servicehub.repository;

import com.servicehub.entity.Booking;
import com.servicehub.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByServiceProviderId(Long providerId);

    long countByServiceProviderId(Long providerId);

    long countByServiceProviderIdAndStatus(Long providerId, BookingStatus status);

    // Is the provider already busy on this day? (cancelled bookings do not count)
    boolean existsByServiceProviderIdAndServiceDateAndStatusNot(
            Long providerId, LocalDate serviceDate, BookingStatus status);

    // Did this customer ever get a COMPLETED service from this provider?
    boolean existsByCustomerIdAndServiceProviderIdAndStatus(
            Long customerId, Long providerId, BookingStatus status);
}