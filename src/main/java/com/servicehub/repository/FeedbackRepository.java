package com.servicehub.repository;

import com.servicehub.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository
extends JpaRepository<Feedback , Long> {

    List<Feedback> findByCustomerId(Long customerID);

    List<Feedback> findByServiceProviderId(Long providerId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f .serviceProvider.id = :providerId")

    Double getAverageRatingByProviderId(Long providerId);

    long countByServiceProviderId(Long providerId);

}
