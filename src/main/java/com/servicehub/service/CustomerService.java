package com.servicehub.service;

import com.servicehub.dto.CustomerLoginDTO;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.InvalidCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.servicehub.repository.CustomerRepository;
import com.servicehub.entity.Customer;
import com.servicehub.dto.CustomerDTO;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder) {

        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer saveCustomer(CustomerDTO customerDTO) {

        Customer customer = new Customer();

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobileNumber(customerDTO.getMobileNumber());
        customer.setPassword(
                passwordEncoder.encode(customerDTO.getPassword()));

        customer.setCreatedAt(LocalDateTime.now());

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer with id " + id + " not found"));
    }

    public Customer updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id " + id + " not found"));

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobileNumber(customerDTO.getMobileNumber());
        customer.setPassword(
                passwordEncoder.encode(customerDTO.getPassword()));

        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer with id " + id + " Not found "));
        customerRepository.delete(customer);
    }

    public Customer login(CustomerLoginDTO loginDTO) {

        Customer customer = customerRepository
                .findByEmail(loginDTO.getEmail())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Invalid email or password"));

        if (!passwordEncoder.matches(
                loginDTO.getPassword(), customer.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }
        return customer;
    }
}