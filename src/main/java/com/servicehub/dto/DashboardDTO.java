package com.servicehub.dto;

public class DashboardDTO {
    private Long totalBookings;
    private Long completeBookings;
    private Long pendingBookings;
    private Long totalFeedback;
    private Double averageRating;


    public DashboardDTO(){

    }

    public Long getTotalBookings() {
        return totalBookings;
    }

    public Long getCompleteBookings() {
        return completeBookings;
    }

    public Long getPendingBookings() {
        return pendingBookings;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public Long getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalBookings(Long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public void setCompleteBookings(Long completeBookings) {
        this.completeBookings = completeBookings;
    }

    public void setPendingBookings(Long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public void setTotalFeedback(Long totalFeedback) {
        this.totalFeedback = totalFeedback;
    }
}
