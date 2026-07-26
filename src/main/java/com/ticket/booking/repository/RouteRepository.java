package com.ticket.booking.repository;

import com.ticket.booking.model.Route;
import com.ticket.booking.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    
    Optional<Route> findByRouteCode(String routeCode);
    
    List<Route> findByStartStation(Station startStation);
    
    List<Route> findByEndStation(Station endStation);
    
    List<Route> findByStartStationAndEndStation(Station startStation, Station endStation);
    
    @Query("SELECT r FROM Route r WHERE r.startStation.city = :startCity AND r.endStation.city = :endCity")
    List<Route> findRoutesBetweenCities(@Param("startCity") String startCity, @Param("endCity") String endCity);
    
    @Query("SELECT r FROM Route r WHERE r.startStation.id = :startStationId AND r.endStation.id = :endStationId")
    List<Route> findRoutesBetweenStations(@Param("startStationId") Long startStationId, @Param("endStationId") Long endStationId);
    
    List<Route> findByRouteType(String routeType);
    
    List<Route> findByStatus(String status);
    
    @Query("SELECT r FROM Route r WHERE r.distanceKm BETWEEN :minDistance AND :maxDistance")
    List<Route> findRoutesByDistanceRange(@Param("minDistance") Integer minDistance, @Param("maxDistance") Integer maxDistance);
}