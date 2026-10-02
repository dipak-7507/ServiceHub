package com.servicehub.service;

import com.servicehub.dto.BookingDTO;
import com.servicehub.entity.Booking;
import com.servicehub.entity.Customer;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.exception.BookingNotFoundException;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.UnauthorizedAccessException;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.ServiceProviderRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    public BookingService(BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          ServiceProviderRepository serviceProviderRepository) {

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.serviceProviderRepository = serviceProviderRepository;
    }

    private String getLoggedInEmail() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }

    private void checkBookingAccess(Booking booking) {
        String email = getLoggedInEmail();

        boolean isCustomer = booking.getCustomer().getEmail().equals(email);
        boolean isProvider = booking.getServiceProvider().getEmail().equals(email);

        if (!isCustomer && !isProvider) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this booking");
        }
    }

    public Booking saveBooking(BookingDTO bookingDTO) {
        Customer customer = customerRepository
                .findById(bookingDTO.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!customer.getEmail().equals(getLoggedInEmail())) {
            throw new UnauthorizedAccessException(
                    "You can only create bookings for yourself");
        }

        ServiceProvider provider = serviceProviderRepository
                .findById(bookingDTO.getProviderId())
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setServiceProvider(provider);
        booking.setServiceType(bookingDTO.getServiceType());
        booking.setBookingDate(LocalDate.now());
        booking.setServiceDate(bookingDTO.getServiceDate());
        booking.setStatus("PENDING");

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));
        checkBookingAccess(booking);
        return booking;
    }

    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));
        checkBookingAccess(booking);

        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));
        checkBookingAccess(booking);

        bookingRepository.delete(booking);
    }

    public List<Booking> getBookingsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!customer.getEmail().equals(getLoggedInEmail())) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this customer's bookings");
        }
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getBookingByProvider(Long providerId) {
        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        if (!provider.getEmail().equals(getLoggedInEmail())) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this provider's bookings");
        }
        return bookingRepository.findByServiceProviderId(providerId);
    }
}