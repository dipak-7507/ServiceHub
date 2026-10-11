package com.servicehub.entity;

public enum BookingStatus {
    PENDING,
    CONFIRMED,
    COMPLETED,
    CANCELLED;

    public boolean providerCanMoveTo(BookingStatus next) {
        return switch(this) {
            case PENDING -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == COMPLETED || next == CANCELLED;
            case COMPLETED , CANCELLED -> false;

        };
    }
    public boolean customerCanMoveTo(BookingStatus next){
        return (this == PENDING || this == CONFIRMED) && next == CANCELLED;
    }
}
