package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "routes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String routeCode;
    
    @Column(nullable = false)
    private String routeName;
    
    @ManyToOne
    @JoinColumn(name = "start_station_id", nullable = false)
    private Station startStation;
    
    @ManyToOne
    @JoinColumn(name = "end_station_id", nullable = false)
    private Station endStation;
    
    @Column(nullable = false)
    private Integer distanceKm; // Distance in kilometers
    
    @Column(nullable = false)
    private Integer estimatedTravelTime; // Estimated travel time in minutes
    
    @Column(nullable = false)
    private String routeType; // Main, Branch, Suburban, etc.
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, UNDER_CONSTRUCTION
    
    @Column(nullable = false)
    private Double baseFare; // Base fare for this route
}