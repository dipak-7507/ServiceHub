package com.servicehub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.servicehub.entity.Customer;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long> {

    Optional<Customer> findByEmail(String email);
}
