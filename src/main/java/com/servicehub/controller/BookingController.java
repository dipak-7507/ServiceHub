package com.servicehub.controller;

import com.servicehub.dto.BookingDTO;
import com.servicehub.dto.BookingResponseDTO;
import com.servicehub.entity.BookingStatus;
import com.servicehub.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingResponseDTO saveBooking(
            @Valid @RequestBody BookingDTO bookingDTO) {
        return BookingResponseDTO.from(bookingService.saveBooking(bookingDTO));
    }

    @GetMapping("/{id}")
    public BookingResponseDTO getBookingById(@PathVariable Long id) {
        return BookingResponseDTO.from(bookingService.getBookingById(id));
    }

    /** Example: PUT /bookings/5/status?status=CONFIRMED */
    @PutMapping("/{id}/status")
    public BookingResponseDTO updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status) {
        return BookingResponseDTO.from(
                bookingService.updateBookingStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return "Booking Deleted Successfully";
    }

    @GetMapping("/customer/{customerId}")
    public List<BookingResponseDTO> getBookingsByCustomer(
            @PathVariable Long customerId) {
        return bookingService.getBookingsByCustomer(customerId).stream()
                .map(BookingResponseDTO::from)
                .toList();
    }

    @GetMapping("/provider/{providerId}")
    public List<BookingResponseDTO> getBookingsByProvider(
            @PathVariable Long providerId) {
        return bookingService.getBookingByProvider(providerId).stream()
                .map(BookingResponseDTO::from)
                .toList();
    }
}