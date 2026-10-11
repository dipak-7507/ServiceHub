package com.servicehub.dto;
import com.servicehub.entity.Booking;
import com.servicehub.entity.BookingStatus;
import java.time.LocalDate;

public record BookingResponseDTO(
        Long id,
        long customerId,
        String customerName,
        Long providerId,
        String providerName,
        String serviceType,
        LocalDate bookingDate,
        LocalDate serviceDate,
        BookingStatus status) {

    public static BookingResponseDTO from (Booking b){
        return new BookingResponseDTO(
                b.getId(),
                b.getCustomer().getId(),
                b.getCustomer().getFirstName() + " " + b.getCustomer().getLastName(),
                b.getServiceProvider().getId(),
                b.getServiceProvider().getFirstName() + " " + b.getServiceProvider().getLastName(),
                b.getServiceType(),
                b.getBookingDate(),
                b.getServiceDate(),
                b.getStatus());

    }
}
