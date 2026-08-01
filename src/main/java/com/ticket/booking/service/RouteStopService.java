package com.ticket.booking.service;

import com.ticket.booking.dto.RouteStopDTO;
import com.ticket.booking.model.Route;
import com.ticket.booking.model.RouteStop;
import com.ticket.booking.model.Station;
import com.ticket.booking.repository.RouteRepository;
import com.ticket.booking.repository.RouteStopRepository;
import com.ticket.booking.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteStopService {
    
    private final RouteStopRepository routeStopRepository;
    private final RouteRepository routeRepository;
    private final StationRepository stationRepository;
    
    @Transactional
    public RouteStopDTO createRouteStop(RouteStopDTO routeStopDTO) {
        Route route = routeRepository.findById(routeStopDTO.getRouteId())
            .orElseThrow(() -> new RuntimeException("Route not found with id: " + routeStopDTO.getRouteId()));
        
        Station station = stationRepository.findById(routeStopDTO.getStation().getId())
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + routeStopDTO.getStation().getId()));
        
        RouteStop routeStop = new RouteStop();
        routeStop.setRoute(route);
        routeStop.setStation(station);
        routeStop.setStopOrder(routeStopDTO.getStopOrder());
        routeStop.setDistanceFromStart(routeStopDTO.getDistanceFromStart());
        routeStop.setEstimatedArrivalOffset(routeStopDTO.getEstimatedArrivalOffset());
        routeStop.setEstimatedDepartureOffset(routeStopDTO.getEstimatedDepartureOffset());
        routeStop.setStopDuration(routeStopDTO.getStopDuration());
        routeStop.setPlatformNumber(routeStopDTO.getPlatformNumber());
        routeStop.setIsIntermediateStop(routeStopDTO.getIsIntermediateStop());
        routeStop.setStopType(routeStopDTO.getStopType());
        routeStop.setFacilitiesAvailable(routeStopDTO.getFacilitiesAvailable());
        routeStop.setStatus(routeStopDTO.getStatus());
        routeStop.setStopFareFromStart(routeStopDTO.getStopFareFromStart());
        
        RouteStop savedStop = routeStopRepository.save(routeStop);
        return convertToDTO(savedStop);
    }
    
    @Transactional
    public RouteStopDTO updateRouteStop(Long id, RouteStopDTO routeStopDTO) {
        RouteStop existingStop = routeStopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route stop not found with id: " + id));
        
        if (routeStopDTO.getStopOrder() != null) {
            existingStop.setStopOrder(routeStopDTO.getStopOrder());
        }
        if (routeStopDTO.getDistanceFromStart() != null) {
            existingStop.setDistanceFromStart(routeStopDTO.getDistanceFromStart());
        }
        if (routeStopDTO.getEstimatedArrivalOffset() != null) {
            existingStop.setEstimatedArrivalOffset(routeStopDTO.getEstimatedArrivalOffset());
        }
        if (routeStopDTO.getEstimatedDepartureOffset() != null) {
            existingStop.setEstimatedDepartureOffset(routeStopDTO.getEstimatedDepartureOffset());
        }
        if (routeStopDTO.getStopDuration() != null) {
            existingStop.setStopDuration(routeStopDTO.getStopDuration());
        }
        if (routeStopDTO.getPlatformNumber() != null) {
            existingStop.setPlatformNumber(routeStopDTO.getPlatformNumber());
        }
        if (routeStopDTO.getIsIntermediateStop() != null) {
            existingStop.setIsIntermediateStop(routeStopDTO.getIsIntermediateStop());
        }
        if (routeStopDTO.getStopType() != null) {
            existingStop.setStopType(routeStopDTO.getStopType());
        }
        if (routeStopDTO.getFacilitiesAvailable() != null) {
            existingStop.setFacilitiesAvailable(routeStopDTO.getFacilitiesAvailable());
        }
        if (routeStopDTO.getStatus() != null) {
            existingStop.setStatus(routeStopDTO.getStatus());
        }
        if (routeStopDTO.getStopFareFromStart() != null) {
            existingStop.setStopFareFromStart(routeStopDTO.getStopFareFromStart());
        }
        
        RouteStop updatedStop = routeStopRepository.save(existingStop);
        return convertToDTO(updatedStop);
    }
    
    @Transactional
    public void deleteRouteStop(Long id) {
        RouteStop routeStop = routeStopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route stop not found with id: " + id));
        routeStopRepository.delete(routeStop);
    }
    
    public RouteStopDTO getRouteStopById(Long id) {
        RouteStop routeStop = routeStopRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route stop not found with id: " + id));
        return convertToDTO(routeStop);
    }
    
    public List<RouteStopDTO> getAllRouteStopsByRouteId(Long routeId) {
        List<RouteStop> routeStops = routeStopRepository.findAllByRouteIdOrderByStopOrder(routeId);
        return routeStops.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<RouteStopDTO> getRouteStopsByRouteIdAndStationRange(Long routeId, Integer startOrder, Integer endOrder) {
        List<RouteStop> routeStops = routeStopRepository.findByRouteIdAndStopOrderBetween(routeId, startOrder, endOrder);
        return routeStops.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<RouteStopDTO> getStopsBetweenStations(Long routeId, Long startStationId, Long endStationId) {
        RouteStop startStop = routeStopRepository.findByRouteIdAndStationId(routeId, startStationId)
            .orElseThrow(() -> new RuntimeException("Start station not found on route"));
        
        RouteStop endStop = routeStopRepository.findByRouteIdAndStationId(routeId, endStationId)
            .orElseThrow(() -> new RuntimeException("End station not found on route"));
        
        int startOrder = startStop.getStopOrder();
        int endOrder = endStop.getStopOrder();
        
        // Ensure start order is less than end order
        if (startOrder > endOrder) {
            int temp = startOrder;
            startOrder = endOrder;
            endOrder = temp;
        }
        
        return getRouteStopsByRouteIdAndStationRange(routeId, startOrder, endOrder);
    }
    
    public List<RouteStopDTO> getRouteStopsByStationId(Long stationId) {
        List<RouteStop> routeStops = routeStopRepository.findByStationId(stationId);
        return routeStops.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public List<RouteStopDTO> getActiveRouteStopsByRouteId(Long routeId) {
        List<RouteStop> routeStops = routeStopRepository.findActiveStopsByRouteId(routeId);
        return routeStops.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public List<RouteStopDTO> generateRouteStopsForRoute(Long routeId, LocalDateTime routeStartTime) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new RuntimeException("Route not found with id: " + routeId));
        
        List<RouteStopDTO> generatedStops = new ArrayList<>();
        
        // Get all stations along this route (this would be based on actual Myanmar railway data)
        // For now, we'll create a mock implementation
        List<Station> stationsAlongRoute = getStationsForRoute(route);
        
        int totalStops = stationsAlongRoute.size();
        int totalDistance = route.getDistanceKm();
        
        for (int i = 0; i < totalStops; i++) {
            Station station = stationsAlongRoute.get(i);
            
            RouteStopDTO stopDTO = new RouteStopDTO();
            stopDTO.setRouteId(routeId);
            stopDTO.setRouteCode(route.getRouteCode());
            stopDTO.setRouteName(route.getRouteName());
            
            // Create station DTO
            com.ticket.booking.dto.StationDTO stationDTO = new com.ticket.booking.dto.StationDTO();
            stationDTO.setId(station.getId());
            stationDTO.setCode(station.getCode());
            stationDTO.setName(station.getName());
            stationDTO.setCity(station.getCity());
            stationDTO.setState(station.getState());
            stationDTO.setPlatformCount(station.getPlatformCount());
            stationDTO.setFacilities(station.getFacilities());
            stopDTO.setStation(stationDTO);
            
            stopDTO.setStopOrder(i + 1);
            
            // Calculate distance proportionally
            int distanceFromStart = (totalDistance * (i + 1)) / totalStops;
            stopDTO.setDistanceFromStart(distanceFromStart);
            
            // Calculate time offsets (assume average speed of 50 km/h)
            int travelMinutes = (distanceFromStart * 60) / 50;
            stopDTO.setEstimatedArrivalOffset(travelMinutes);
            
            // Stop duration: 5-15 minutes for intermediate stops, 0 for start/end
            int stopDuration = (i > 0 && i < totalStops - 1) ? 5 + (i % 10) : 0;
            stopDTO.setStopDuration(stopDuration);
            stopDTO.setEstimatedDepartureOffset(travelMinutes + stopDuration);
            
            stopDTO.setPlatformNumber(String.valueOf((i % 3) + 1)); // Platform 1-3
            stopDTO.setIsIntermediateStop(i > 0 && i < totalStops - 1);
            stopDTO.setStopType(getStopType(i, totalStops));
            stopDTO.setFacilitiesAvailable(getFacilitiesForStop(station, i));
            stopDTO.setStatus("ACTIVE");
            
            // Calculate fare proportionally
            double fareFromStart = route.getBaseFare() * ((double) distanceFromStart / totalDistance);
            stopDTO.setStopFareFromStart(fareFromStart);
            
            // Calculate times if routeStartTime is provided
            if (routeStartTime != null) {
                stopDTO.setCalculatedArrivalTime(routeStartTime.plusMinutes(stopDTO.getEstimatedArrivalOffset()));
                stopDTO.setCalculatedDepartureTime(routeStartTime.plusMinutes(stopDTO.getEstimatedDepartureOffset()));
            }
            
            stopDTO.setIsStartStation(i == 0);
            stopDTO.setIsEndStation(i == totalStops - 1);
            stopDTO.setStopInfo(String.format("Stop #%d: %s - Arrival: +%d min, Stop: %d min", 
                stopDTO.getStopOrder(), station.getName(), stopDTO.getEstimatedArrivalOffset(), stopDTO.getStopDuration()));
            
            // Create and save the stop
            RouteStopDTO createdStop = createRouteStop(stopDTO);
            generatedStops.add(createdStop);
        }
        
        return generatedStops;
    }
    
    private List<Station> getStationsForRoute(Route route) {
        // This should be based on actual Myanmar railway route data
        // For now, return a mock list of stations
        List<Station> stations = new ArrayList<>();
        
        // Add start station
        stations.add(route.getStartStation());
        
        // Add intermediate stations based on route
        String routeCode = route.getRouteCode();
        
        // Mock intermediate stations for different Myanmar routes
        if (routeCode.contains("YGN-MDY")) {
            // Yangon to Mandalay route
            stations.addAll(stationRepository.findByCityIn(List.of("Bago", "Pyay", "Taungoo", "Naypyitaw")));
        } else if (routeCode.contains("YGN-MLM")) {
            // Yangon to Mawlamyine route
            stations.addAll(stationRepository.findByCityIn(List.of("Bago", "Kyaikto", "Thaton")));
        } else if (routeCode.contains("MDY-MYK")) {
            // Mandalay to Myitkyina route
            stations.addAll(stationRepository.findByCityIn(List.of("Mogok", "Bhamo", "Shwegu")));
        } else {
            // Generic route - add some stations
            List<Station> allStations = stationRepository.findAll();
            int totalStations = Math.min(5, allStations.size() - 2); // Add up to 5 intermediate stations
            
            // Filter out start and end stations
            List<Station> availableStations = allStations.stream()
                .filter(s -> !s.getId().equals(route.getStartStation().getId()) && 
                            !s.getId().equals(route.getEndStation().getId()))
                .limit(totalStations)
                .collect(Collectors.toList());
            
            stations.addAll(availableStations);
        }
        
        // Add end station
        stations.add(route.getEndStation());
        
        return stations;
    }
    
    private String getStopType(int index, int totalStops) {
        if (index == 0 || index == totalStops - 1) {
            return "TERMINAL";
        } else if (index == 1 || index == totalStops - 2) {
            return "MAJOR";
        } else {
            return "REGULAR";
        }
    }
    
    private String getFacilitiesForStop(Station station, int index) {
        List<String> facilities = new ArrayList<>();
        
        if (station.getFacilities() != null) {
            facilities.add(station.getFacilities());
        }
        
        if (index == 0 || index == 5) { // Every 5th stop gets refreshment facilities
            facilities.add("Refreshment stalls");
        }
        
        if (index % 3 == 0) { // Every 3rd stop gets waiting room
            facilities.add("Waiting room");
        }
        
        return String.join(", ", facilities);
    }
    
    private RouteStopDTO convertToDTO(RouteStop routeStop) {
        RouteStopDTO dto = new RouteStopDTO();
        dto.setId(routeStop.getId());
        dto.setRouteId(routeStop.getRoute().getId());
        dto.setRouteCode(routeStop.getRoute().getRouteCode());
        dto.setRouteName(routeStop.getRoute().getRouteName());
        
        // Convert station to DTO
        Station station = routeStop.getStation();
        com.ticket.booking.dto.StationDTO stationDTO = new com.ticket.booking.dto.StationDTO();
        stationDTO.setId(station.getId());
        stationDTO.setCode(station.getCode());
        stationDTO.setName(station.getName());
        stationDTO.setCity(station.getCity());
        stationDTO.setState(station.getState());
        stationDTO.setPlatformCount(station.getPlatformCount());
        stationDTO.setFacilities(station.getFacilities());
        dto.setStation(stationDTO);
        
        dto.setStopOrder(routeStop.getStopOrder());
        dto.setDistanceFromStart(routeStop.getDistanceFromStart());
        dto.setEstimatedArrivalOffset(routeStop.getEstimatedArrivalOffset());
        dto.setEstimatedDepartureOffset(routeStop.getEstimatedDepartureOffset());
        dto.setStopDuration(routeStop.getStopDuration());
        dto.setPlatformNumber(routeStop.getPlatformNumber());
        dto.setIsIntermediateStop(routeStop.getIsIntermediateStop());
        dto.setStopType(routeStop.getStopType());
        dto.setFacilitiesAvailable(routeStop.getFacilitiesAvailable());
        dto.setStatus(routeStop.getStatus());
        dto.setStopFareFromStart(routeStop.getStopFareFromStart());
        
        // Calculate derived fields
        dto.setIsStartStation(routeStop.isStartStation());
        dto.setIsEndStation(false); // This would need to be calculated based on route stops
        dto.setStopInfo(routeStop.getStopInfo());
        
        return dto;
    }
}