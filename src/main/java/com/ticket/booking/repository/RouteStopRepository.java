package com.ticket.booking.repository;

import com.ticket.booking.model.Route;
import com.ticket.booking.model.RouteStop;
import com.ticket.booking.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteStopRepository extends JpaRepository<RouteStop, Long> {
    
    List<RouteStop> findByRoute(Route route);
    
    List<RouteStop> findByRouteId(Long routeId);
    
    List<RouteStop> findByStation(Station station);
    
    List<RouteStop> findByStationId(Long stationId);
    
    List<RouteStop> findByRouteAndStopOrderBetween(Route route, Integer startOrder, Integer endOrder);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id = :routeId ORDER BY rs.stopOrder ASC")
    List<RouteStop> findAllByRouteIdOrderByStopOrder(@Param("routeId") Long routeId);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id = :routeId AND rs.station.id = :stationId")
    Optional<RouteStop> findByRouteIdAndStationId(@Param("routeId") Long routeId, @Param("stationId") Long stationId);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id = :routeId AND rs.stopOrder = :stopOrder")
    Optional<RouteStop> findByRouteIdAndStopOrder(@Param("routeId") Long routeId, @Param("stopOrder") Integer stopOrder);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id = :routeId AND rs.stopOrder >= :startOrder AND rs.stopOrder <= :endOrder ORDER BY rs.stopOrder ASC")
    List<RouteStop> findByRouteIdAndStopOrderBetween(@Param("routeId") Long routeId, 
                                                     @Param("startOrder") Integer startOrder, 
                                                     @Param("endOrder") Integer endOrder);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.startStation.id = :startStationId OR rs.route.endStation.id = :endStationId")
    List<RouteStop> findStopsByRouteEndpoints(@Param("startStationId") Long startStationId, 
                                              @Param("endStationId") Long endStationId);
    
    @Query("SELECT COUNT(rs) FROM RouteStop rs WHERE rs.route.id = :routeId")
    Integer countStopsByRouteId(@Param("routeId") Long routeId);
    
    @Query("SELECT MAX(rs.stopOrder) FROM RouteStop rs WHERE rs.route.id = :routeId")
    Integer findMaxStopOrderByRouteId(@Param("routeId") Long routeId);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id IN :routeIds ORDER BY rs.route.id, rs.stopOrder")
    List<RouteStop> findAllByRouteIdsOrdered(@Param("routeIds") List<Long> routeIds);
    
    @Query("SELECT DISTINCT rs.route FROM RouteStop rs WHERE rs.station.id = :stationId")
    List<Route> findRoutesByStationId(@Param("stationId") Long stationId);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.id = :routeId AND rs.status = 'ACTIVE' ORDER BY rs.stopOrder ASC")
    List<RouteStop> findActiveStopsByRouteId(@Param("routeId") Long routeId);
    
    @Query("SELECT rs FROM RouteStop rs WHERE rs.route.routeCode = :routeCode ORDER BY rs.stopOrder ASC")
    List<RouteStop> findByRouteCode(@Param("routeCode") String routeCode);
}