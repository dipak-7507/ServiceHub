package com.servicehub.controller;


import com.servicehub.dto.BookingDTO;
import com.servicehub.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import com.servicehub.entity.Booking;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController (BookingService bookingService){

        this.bookingService = bookingService;

    }
    @PostMapping
    public Booking saveBooking(
            @Valid @RequestBody BookingDTO bookingDTO){
        return bookingService.saveBooking(bookingDTO);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@PathVariable Long id){
        return bookingService.getBookingById(id);

    }

    @PutMapping("/{id}/status")
    public Booking updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status){
        return bookingService.updateBookingStatus(id, status);

    }
    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable Long id){

        bookingService.deleteBooking(id);

        return  "Booking Deleted Successfully";

    }
    @GetMapping
    public List<Booking>getAllBookings(){
        return bookingService.getAllBookings();

    }

     @GetMapping("/customer/{customerId}")
    public List<Booking> getBookingsByCustomer(
            @PathVariable Long customerId){

        return bookingService.getBookingsByCustomer(customerId);

    }

    @GetMapping("/provider/{providerId}")
    public List<Booking> getBookingsByProvider(
            @PathVariable Long providerId){

        return bookingService.getBookingByProvider(providerId);
    }

}
