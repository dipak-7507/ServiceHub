package com.servicehub.service;

import com.servicehub.dto.CustomerDTO;
import com.servicehub.dto.CustomerLoginDTO;
import com.servicehub.entity.Customer;
import com.servicehub.exception.CustomerNotFoundException;
import com.servicehub.exception.InvalidCredentialsException;
import com.servicehub.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Test");
        customer.setLastName("User");
        customer.setEmail("test@example.com");
        customer.setPassword("encodedPassword");
    }

    @Test
    void saveCustomer_shouldEncodePasswordAndSave() {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName("Test");
        dto.setLastName("User");
        dto.setEmail("test@example.com");
        dto.setMobileNumber("9999999999");
        dto.setPassword("plainPassword");

        when(passwordEncoder.encode("plainPassword"))
                .thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class)))
                .thenReturn(customer);

        Customer result = customerService.saveCustomer(dto);

        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder, times(1)).encode("plainPassword");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerById_whenFound_shouldReturnCustomer() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerById(1L);

        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void getCustomerById_whenNotFound_shouldThrowException() {
        when(customerRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class,
                () -> customerService.getCustomerById(99L));
    }

    @Test
    void login_withCorrectPassword_shouldReturnCustomer() {
        CustomerLoginDTO loginDTO = new CustomerLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("plainPassword");

        when(customerRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("plainPassword", "encodedPassword"))
                .thenReturn(true);

        Customer result = customerService.login(loginDTO);

        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void login_withWrongPassword_shouldThrowException() {
        CustomerLoginDTO loginDTO = new CustomerLoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword("wrongPassword");

        when(customerRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(customer));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword"))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> customerService.login(loginDTO));
    }

    @Test
    void deleteCustomer_whenFound_shouldCallRepositoryDelete() {
        when(customerRepository.findById(1L))
                .thenReturn(Optional.of(customer));

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).delete(customer);
    }
}