package com.servicehub.repository;

import com.servicehub.entity.ServiceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ServiceCategoryRepository
        extends JpaRepository<ServiceCategory, Long>{
}
