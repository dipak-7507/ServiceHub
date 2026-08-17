package com.servicehub.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDTO {

    @NotNull
    private Long customerId;


    @NotNull
    private Long providerId;


    @NotBlank
    private String serviceType;

    @NotNull
    private LocalDate serviceDate;

    private String status;
}
