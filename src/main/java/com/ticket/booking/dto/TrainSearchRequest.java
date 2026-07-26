package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainSearchRequest {
    private String sourceCity;
    private String destinationCity;
    private LocalDateTime journeyDate;
    private Integer numberOfPassengers = 1;
    private String trainType; // Optional
    private String coachType; // Optional
}