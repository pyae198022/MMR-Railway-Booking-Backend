package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {
    private Long id;
    private String seatNumber;
    private String coachType;
    private String seatType;
    private Double priceMultiplier;
    private Boolean isAvailable;
    private String status;
    private Double calculatedPrice;
}