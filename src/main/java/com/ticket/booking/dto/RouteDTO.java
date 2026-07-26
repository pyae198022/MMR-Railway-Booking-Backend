package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {
    private Long id;
    private String routeCode;
    private String routeName;
    private StationDTO startStation;
    private StationDTO endStation;
    private Integer distanceKm;
    private Integer estimatedTravelTime;
    private String routeType;
    private String description;
    private String status;
    private Double baseFare;
}