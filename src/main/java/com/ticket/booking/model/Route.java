package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    
    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RouteStop> stops = new ArrayList<>();
    
    // Helper methods
    public int getTotalStops() {
        return stops != null ? stops.size() : 0;
    }
    
    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        if (stops != null) {
            for (RouteStop stop : stops) {
                stations.add(stop.getStation());
            }
        }
        return stations;
    }
    
    public RouteStop getStopByOrder(int order) {
        if (stops != null) {
            for (RouteStop stop : stops) {
                if (stop.getStopOrder() == order) {
                    return stop;
                }
            }
        }
        return null;
    }
    
    public List<RouteStop> getIntermediateStops() {
        List<RouteStop> intermediateStops = new ArrayList<>();
        if (stops != null) {
            for (RouteStop stop : stops) {
                if (stop.getIsIntermediateStop() != null && stop.getIsIntermediateStop()) {
                    intermediateStops.add(stop);
                }
            }
        }
        return intermediateStops;
    }
}