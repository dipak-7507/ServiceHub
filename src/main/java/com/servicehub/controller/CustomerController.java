package com.servicehub.controller;

import com.servicehub.dto.CustomerLoginDTO;
import com.servicehub.dto.CustomerDTO;
import com.servicehub.service.CustomerService;
import jakarta.validation.Valid;
import com.servicehub.entity.Customer;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import com.servicehub.security.JwtUtil;
import com.servicehub.dto.LoginResponseDTO;



@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JwtUtil jwtUtil;

    public CustomerController(CustomerService customerService, JwtUtil jwtUtil) {

        this.customerService = customerService;
        this.jwtUtil =jwtUtil;
    }

    @PostMapping

    public Customer saveCustomer(@Valid @RequestBody CustomerDTO customerDTO) {

        return customerService.saveCustomer(customerDTO);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {

        return customerService.getCustomerById(id);
    }

    @PutMapping("/{id}")

    public Customer updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO
    ) {
        return customerService.updateCustomer(id, customerDTO);
    }

    @DeleteMapping("/{id}")

    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            @Valid @RequestBody CustomerLoginDTO loginDTO) {

      Customer customer =
              customerService.login(loginDTO);

        String token =
                jwtUtil.generateToken(customer.getEmail());

        return new LoginResponseDTO(token);
    }

}