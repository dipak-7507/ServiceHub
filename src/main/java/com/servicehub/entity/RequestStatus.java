package com.servicehub.entity;

public enum RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED,
    COMPLETED,
    CANCELLED;

    public boolean providerCanMoveTo(RequestStatus next) {
        return switch (this) {
            case PENDING -> next == ACCEPTED || next == REJECTED;
            case ACCEPTED -> next == COMPLETED;
            case REJECTED, COMPLETED, CANCELLED -> false;
        };
    }

    public boolean customerCanMoveTo(RequestStatus next) {
        return (this == PENDING || this == ACCEPTED) && next == CANCELLED;
    }
}