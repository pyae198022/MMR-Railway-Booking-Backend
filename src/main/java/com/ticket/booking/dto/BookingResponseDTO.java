package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDTO {
    private Long id;
    private String pnrNumber;
    private UserDTO user;
    private TrainDTO train;
    private StationDTO sourceStation;
    private StationDTO destinationStation;
    private LocalDateTime bookingDate;
    private LocalDateTime journeyDate;
    private Integer numberOfPassengers;
    private Double totalFare;
    private Double taxAmount;
    private Double grandTotal;
    private String bookingStatus;
    private String paymentStatus;
    private List<PassengerDTO> passengers;
    private PaymentDTO payment;
}