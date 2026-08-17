package com.servicehub.service;

import com.servicehub.dto.BookingDTO;
import com.servicehub.entity.Customer;
import com.servicehub.entity.ServiceProvider;
import com.servicehub.repository.BookingRepository;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.repository.ServiceProviderRepository;
import org.springframework.stereotype.Service;
import com.servicehub.entity.Booking;

import java.util.List;
import java.time.LocalDate;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final ServiceProviderRepository serviceProviderRepository;

    public BookingService(BookingRepository bookingRepository,
                          CustomerRepository customerRepository,
                          ServiceProviderRepository serviceProviderRepository){

        this.bookingRepository = bookingRepository;
        this.customerRepository = customerRepository;
        this.serviceProviderRepository = serviceProviderRepository;

    }
    public Booking saveBooking(BookingDTO bookingDTO){
        Customer customer =customerRepository
                .findById(bookingDTO.getCustomerId())
                .orElseThrow(()->
                        new RuntimeException("Customer not found"));

        ServiceProvider provider = serviceProviderRepository
                .findById(bookingDTO.getProviderId())
                .orElseThrow(()->
                        new RuntimeException("Provider not found"));

        Booking booking = new Booking();

        booking.setCustomer(customer);
        booking.setServiceProvider(provider);
        booking.setServiceType(bookingDTO.getServiceType());
        booking.setBookingDate(LocalDate.now());
        booking.setServiceDate(bookingDTO.getServiceDate());
        booking.setStatus("PENDING");

       return bookingRepository.save(booking);
    }
    public List<Booking>getAllBookings(){
        return bookingRepository.findAll();
    }
    public Booking getBookingById(Long id){
        return bookingRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Booking not found"));

    }
    public Booking updateBookingStatus(Long id, String status){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Booking not found"));

        booking.setStatus(status);

        return bookingRepository.save(booking);

    }

    public void deleteBooking(Long id){
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Booking not found"));

        bookingRepository.delete(booking);
    }


    public List<Booking> getBookingsByCustomer(Long customerId){

        return bookingRepository.findByCustomerId(customerId);
    }

    public List<Booking> getBookingByProvider(Long providerId){

        return bookingRepository.findByServiceProviderId(providerId);
    }
}
