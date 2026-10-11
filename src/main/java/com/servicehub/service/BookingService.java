package com.servicehub.service;

import com.servicehub.dto.BookingDTO;
import com.servicehub.entity.Booking;
import com.servicehub.entity.BookingStatus;
import com.servicehub.entity.Customer;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.exception.BookingNotFoundException;
import com.servicehub.exception.ConflictException;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.InvalidRequestException;
import com.servicehub.exception.ProviderNotFoundException;
import com.servicehub.exception.UnauthorizedAccessException;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.ServiceProviderRepository;
import com.servicehub.security.SecurityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // ---------- ownership helpers ----------

    private boolean isBookingCustomer(Booking booking) {
        return SecurityUtil.isLoggedInCustomer(booking.getCustomer().getEmail());
    }

    private boolean isBookingProvider(Booking booking) {
        return SecurityUtil.isLoggedInProvider(booking.getServiceProvider().getEmail());
    }

    private void checkBookingAccess(Booking booking) {
        if (!isBookingCustomer(booking) && !isBookingProvider(booking)) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this booking");
        }
    }

    private Booking findBooking(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found"));
    }

    // ---------- use cases ----------

    @Transactional
    public Booking saveBooking(BookingDTO bookingDTO) {
        Customer customer = customerRepository
                .findById(bookingDTO.getCustomerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        // a customer may only book for himself
        if (!SecurityUtil.isLoggedInCustomer(customer.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You can only create bookings for yourself");
        }

        ServiceProvider provider = serviceProviderRepository
                .findById(bookingDTO.getProviderId())
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        // rule 1: no bookings in the past
        if (bookingDTO.getServiceDate().isBefore(LocalDate.now())) {
            throw new InvalidRequestException(
                    "Service date cannot be in the past");
        }

        // rule 2: a provider can have only one active booking per day
        boolean providerBusy = bookingRepository
                .existsByServiceProviderIdAndServiceDateAndStatusNot(
                        bookingDTO.getProviderId(),
                        bookingDTO.getServiceDate(),
                        BookingStatus.CANCELLED);
        if (providerBusy) {
            throw new ConflictException(
                    "Provider is already booked on this date");
        }

        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setServiceProvider(provider);
        booking.setServiceType(bookingDTO.getServiceType());
        booking.setBookingDate(LocalDate.now());
        booking.setServiceDate(bookingDTO.getServiceDate());
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        Booking booking = findBooking(id);
        checkBookingAccess(booking);
        return booking;
    }

    @Transactional
    public Booking updateBookingStatus(Long id, BookingStatus newStatus) {
        Booking booking = findBooking(id);
        checkBookingAccess(booking);

        BookingStatus current = booking.getStatus();

        if (isBookingProvider(booking)) {
            if (!current.providerCanMoveTo(newStatus)) {
                throw new InvalidRequestException(
                        "Cannot change booking from " + current + " to " + newStatus);
            }
        } else {
            // the logged-in user is the customer of this booking
            if (newStatus != BookingStatus.CANCELLED) {
                throw new UnauthorizedAccessException(
                        "Customers can only cancel a booking");
            }
            if (!current.customerCanMoveTo(newStatus)) {
                throw new InvalidRequestException(
                        "Cannot change booking from " + current + " to " + newStatus);
            }
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Long id) {
        Booking booking = findBooking(id);

        // only the customer can delete, and only before the provider reacts
        if (!isBookingCustomer(booking)) {
            throw new UnauthorizedAccessException(
                    "Only the customer can delete this booking");
        }
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidRequestException(
                    "Only a PENDING booking can be deleted. Cancel it instead.");
        }

        bookingRepository.delete(booking);
    }

    public List<Booking> getBookingsByCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer not found"));

        if (!SecurityUtil.isLoggedInCustomer(customer.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this customer's bookings");
        }
        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getBookingByProvider(Long providerId) {
        ServiceProvider provider = serviceProviderRepository.findById(providerId)
                .orElseThrow(() ->
                        new ProviderNotFoundException("Provider not found"));

        if (!SecurityUtil.isLoggedInProvider(provider.getEmail())) {
            throw new UnauthorizedAccessException(
                    "You are not allowed to access this provider's bookings");
        }
        return bookingRepository.findByServiceProviderId(providerId);
    }
}