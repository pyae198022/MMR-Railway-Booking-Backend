package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteStopDTO {
    private Long id;
    private Long routeId;
    private String routeCode;
    private String routeName;
    private StationDTO station;
    private Integer stopOrder;
    private Integer distanceFromStart; // km
    private Integer estimatedArrivalOffset; // minutes from route start
    private Integer estimatedDepartureOffset; // minutes from route start
    private Integer stopDuration; // minutes
    private String platformNumber;
    private Boolean isIntermediateStop;
    private String stopType;
    private String facilitiesAvailable;
    private String status;
    private Double stopFareFromStart;
    
    // Calculated fields for frontend display
    private LocalDateTime calculatedArrivalTime;
    private LocalDateTime calculatedDepartureTime;
    private Boolean isStartStation;
    private Boolean isEndStation;
    private String stopInfo;
    
    // For train-specific stops
    private Long trainId;
    private String trainNumber;
    private String trainName;
}