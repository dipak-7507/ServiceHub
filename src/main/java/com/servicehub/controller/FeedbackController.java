package com.servicehub.controller;

import com.servicehub.dto.FeedbackDTO;
import com.servicehub.dto.FeedbackResponseDTO;
import com.servicehub.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public FeedbackResponseDTO saveFeedback(
            @Valid @RequestBody FeedbackDTO feedbackDTO) {
        return FeedbackResponseDTO.from(feedbackService.saveFeedback(feedbackDTO));
    }

    @GetMapping("/provider/{providerId}")
    public List<FeedbackResponseDTO> getFeedbacksByProvider(
            @PathVariable Long providerId) {
        return feedbackService.getFeedbacksByProvider(providerId).stream()
                .map(FeedbackResponseDTO::from)
                .toList();
    }

    @GetMapping("/provider/{providerId}/average")
    public Double getAverageRating(@PathVariable Long providerId) {
        return feedbackService.getAverageRatingByProvider(providerId);
    }

    @GetMapping("/{id}")
    public FeedbackResponseDTO getFeedbackById(@PathVariable Long id) {
        return FeedbackResponseDTO.from(feedbackService.getFeedbackById(id));
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return "Feedback Deleted Successfully";
    }

    @GetMapping("/customer/{customerId}")
    public List<FeedbackResponseDTO> getFeedbackByCustomer(
            @PathVariable Long customerId) {
        return feedbackService.getFeedbackByCustomer(customerId).stream()
                .map(FeedbackResponseDTO::from)
                .toList();
    }
}