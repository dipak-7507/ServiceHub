package com.servicehub.repository;
import com.servicehub.entity.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository
            extends JpaRepository<ServiceProvider,Long> {
    List<ServiceProvider>findByServiceCategoryCategoryName(String categoryName);
    Optional<ServiceProvider> findByEmail(String email);
    }

