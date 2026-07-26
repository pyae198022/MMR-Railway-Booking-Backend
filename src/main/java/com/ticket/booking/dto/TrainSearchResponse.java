package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainSearchResponse {
    private TrainDTO train;
    private List<SeatDTO> availableSeats;
    private Double totalAvailableSeatsPrice;
    private String travelDuration; // formatted duration
    private Boolean hasEnoughSeats;
}