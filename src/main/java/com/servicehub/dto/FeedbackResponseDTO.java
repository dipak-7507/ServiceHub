package com.servicehub.dto;

import com.servicehub.entity.Feedback;

import java.time.LocalDateTime;

public record FeedbackResponseDTO(
        Long id,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Long customerId,
        String customerName,
        Long providerId) {

    public static FeedbackResponseDTO from(Feedback f) {
        return new FeedbackResponseDTO(
                f.getId(),
                f.getRating(),
                f.getComment(),
                f.getCreatedAt(),
                f.getCustomer().getId(),
                f.getCustomer().getFirstName() + " " + f.getCustomer().getLastName(),
                f.getServiceProvider().getId());
    }
}