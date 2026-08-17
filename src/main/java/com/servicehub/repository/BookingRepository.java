package com.servicehub.repository;

import com.servicehub.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository

       extends JpaRepository<Booking, Long> {

    List<Booking> findByCustomerId(Long customerId);

    List<Booking> findByServiceProviderId(Long providerId);

    long countByServiceProviderId(Long providerId);

    long countByServiceProviderIdAndStatus(
            Long providerId,
            String status);
}
