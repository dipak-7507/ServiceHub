package com.servicehub.controller;

import com.servicehub.dto.FeedbackDTO;
import com.servicehub.entity.Feedback;
import com.servicehub.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(
            FeedbackService feedbackService) {

        this.feedbackService = feedbackService;
    }

    @PostMapping
    public Feedback saveFeedback(
            @Valid @RequestBody FeedbackDTO feedbackDTO) {

        return feedbackService.saveFeedback(feedbackDTO);
    }
    @GetMapping
    public List<Feedback> getAllFeedbacks(){

        return feedbackService.getAllFeedbacks();
    }

    @GetMapping("/provider/{providerId}")
    public List<Feedback> getFeedbacksByProvider(
            @PathVariable Long providerId){

        return feedbackService
                .getFeedbacksByProvider(providerId);
    }
    @GetMapping("/provider/{providerId}/average")

    public Double getAverageRating(
            @PathVariable Long providerId){
        return  feedbackService
                .getAverageRatingByProvider(providerId);
    }
     @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id){

        return feedbackService.getFeedbackById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteFeedback(@PathVariable Long id){
        feedbackService.deleteFeedback(id);

        return "Feedback Delete Successfully";
    }

    @GetMapping("/customer/{customerId}")
    public List<Feedback> getFeedbackByCustomer(
            @PathVariable Long customerId){

        return feedbackService.getFeedbackByCustomer(customerId);

    }
}