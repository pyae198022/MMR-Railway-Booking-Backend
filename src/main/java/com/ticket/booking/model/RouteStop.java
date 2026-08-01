package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "route_stops", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "station_id", "stop_order"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteStop {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;
    
    @Column(nullable = false)
    private Integer stopOrder; // 1, 2, 3, etc.
    
    @Column(nullable = false)
    private Integer distanceFromStart; // Distance in km from start station
    
    @Column(nullable = false)
    private Integer estimatedArrivalOffset; // Minutes from route start
    
    @Column(nullable = false)
    private Integer estimatedDepartureOffset; // Minutes from route start (stop duration)
    
    @Column(nullable = false)
    private Integer stopDuration; // Minutes the train stops at this station
    
    @Column
    private String platformNumber; // Platform number at this station
    
    @Column
    private Boolean isIntermediateStop = true; // True for stops, false for start/end stations
    
    @Column
    private String stopType; // Regular, Technical, Major, Minor
    
    @Column(columnDefinition = "TEXT")
    private String facilitiesAvailable; // Facilities available during stop
    
    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, TEMPORARILY_CLOSED
    
    @Column
    private Double stopFareFromStart; // Fare from start station to this stop
    
    // Convenience methods
    public LocalDateTime calculateArrivalTime(LocalDateTime routeStartTime) {
        return routeStartTime.plusMinutes(estimatedArrivalOffset);
    }
    
    public LocalDateTime calculateDepartureTime(LocalDateTime routeStartTime) {
        return routeStartTime.plusMinutes(estimatedDepartureOffset);
    }
    
    public boolean isStartStation() {
        return stopOrder == 1;
    }
    
    public boolean isEndStation() {
        Route route = this.route;
        if (route != null) {
            // We need to know total stops to determine if this is end station
            // This should be calculated based on route stops
            return false; // Placeholder
        }
        return false;
    }
    
    public String getStopInfo() {
        return String.format("Stop #%d: %s (%s) - Arrival: +%d min, Stop: %d min", 
                stopOrder, station.getName(), station.getCode(), 
                estimatedArrivalOffset, stopDuration);
    }
}