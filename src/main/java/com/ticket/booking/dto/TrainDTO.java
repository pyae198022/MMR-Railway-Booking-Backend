package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainDTO {
    private Long id;
    private String trainNumber;
    private String trainName;
    private StationDTO sourceStation;
    private StationDTO destinationStation;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double basePrice;
    private String trainType;
    private String status;
    private Long travelDuration; // in minutes
    private Double calculatedPrice;
}