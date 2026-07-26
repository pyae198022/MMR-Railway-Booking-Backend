package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    private Long trainId;
    private Long userId;
    private Long sourceStationId;
    private Long destinationStationId;
    private LocalDateTime journeyDate;
    private List<PassengerRequestDTO> passengers;
    private String paymentMethod;
}