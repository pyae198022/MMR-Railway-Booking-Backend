package com.ticket.booking.service.impl;

import com.ticket.booking.dto.TrainDTO;
import com.ticket.booking.dto.TrainSearchRequest;
import com.ticket.booking.dto.TrainSearchResponse;
import com.ticket.booking.dto.StationDTO;
import com.ticket.booking.model.Station;
import com.ticket.booking.model.Train;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.repository.TrainRepository;
import com.ticket.booking.service.TrainService;
import com.ticket.booking.service.MockRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainServiceImpl implements TrainService {
    
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final MockRouteService mockRouteService;
    
    @Override
    public TrainDTO createTrain(TrainDTO trainDTO) {
        Train train = mapToEntity(trainDTO);
        Train savedTrain = trainRepository.save(train);
        return mapToDTO(savedTrain);
    }
    
    @Override
    public TrainDTO updateTrain(Long id, TrainDTO trainDTO) {
        Train existingTrain = trainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Train not found with id: " + id));
        
        // Update train properties
        existingTrain.setTrainNumber(trainDTO.getTrainNumber());
        existingTrain.setTrainName(trainDTO.getTrainName());
        existingTrain.setDepartureTime(trainDTO.getDepartureTime());
        existingTrain.setArrivalTime(trainDTO.getArrivalTime());
        existingTrain.setTotalSeats(trainDTO.getTotalSeats());
        existingTrain.setAvailableSeats(trainDTO.getAvailableSeats());
        existingTrain.setBasePrice(trainDTO.getBasePrice());
        existingTrain.setTrainType(trainDTO.getTrainType());
        existingTrain.setStatus(trainDTO.getStatus());
        
        // Update stations
        Station sourceStation = stationRepository.findById(trainDTO.getSourceStation().getId())
            .orElseThrow(() -> new RuntimeException("Source station not found"));
        Station destStation = stationRepository.findById(trainDTO.getDestinationStation().getId())
            .orElseThrow(() -> new RuntimeException("Destination station not found"));
        
        existingTrain.setSourceStation(sourceStation);
        existingTrain.setDestinationStation(destStation);
        
        Train updatedTrain = trainRepository.save(existingTrain);
        return mapToDTO(updatedTrain);
    }
    
    @Override
    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }
    
    @Override
    public TrainDTO getTrainById(Long id) {
        Train train = trainRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Train not found with id: " + id));
        return mapToDTO(train);
    }
    
    @Override
    public TrainDTO getTrainByNumber(String trainNumber) {
        Train train = trainRepository.findByTrainNumber(trainNumber)
            .orElseThrow(() -> new RuntimeException("Train not found with number: " + trainNumber));
        return mapToDTO(train);
    }
    
    @Override
    public List<TrainDTO> getAllTrains() {
        return trainRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TrainDTO> getTrainsByType(String trainType) {
        return trainRepository.findByTrainType(trainType).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TrainDTO> getActiveTrains() {
        return trainRepository.findByStatus("ACTIVE").stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<TrainSearchResponse> searchTrains(TrainSearchRequest searchRequest) {
        // This is a simplified implementation - in real app, you'd want more complex logic
        LocalDateTime journeyDate = searchRequest.getJourneyDate();
        LocalDateTime startOfDay = journeyDate.toLocalDate().atStartOfDay();
        LocalDateTime nextDay = startOfDay.plusDays(1);
        
        List<Train> trains = trainRepository.findTrainsBetweenCitiesOnDate(
            searchRequest.getSourceCity(),
            searchRequest.getDestinationCity(),
            startOfDay,
            nextDay
        );
        
        // If we don't have enough trains, use mock service to generate exactly 5 trains
        if (trains.size() < 5) {
            // Find stations for the cities
            List<Station> sourceStations = stationRepository.findByCity(searchRequest.getSourceCity());
            List<Station> destStations = stationRepository.findByCity(searchRequest.getDestinationCity());
            
            if (!sourceStations.isEmpty() && !destStations.isEmpty()) {
                Station sourceStation = sourceStations.get(0);
                Station destStation = destStations.get(0);
                
                // Generate mock trains
                List<Train> mockTrains = mockRouteService.generateMockRoutes(sourceStation, destStation, journeyDate);
                
                // Add mock trains to reach exactly 5 trains total
                int trainsNeeded = 5 - trains.size();
                for (int i = 0; i < Math.min(trainsNeeded, mockTrains.size()); i++) {
                    trains.add(mockTrains.get(i));
                }
            }
        }
        
        // Ensure we have at least 5 trains (if possible)
        // If we have more than 5, limit to first 5 for consistency
        List<Train> finalTrains = trains.size() > 5 ? trains.subList(0, 5) : trains;
        
        return finalTrains.stream()
            .map(train -> {
                TrainSearchResponse response = new TrainSearchResponse();
                response.setTrain(mapToDTO(train));
                
                // Calculate travel duration
                Duration duration = Duration.between(train.getDepartureTime(), train.getArrivalTime());
                response.setTravelDuration(String.format("%dh %dm", duration.toHours(), duration.toMinutesPart()));
                
                // Check if train has enough seats
                response.setHasEnoughSeats(train.getAvailableSeats() >= searchRequest.getNumberOfPassengers());
                
                return response;
            })
            .collect(Collectors.toList());
    }
    
    @Override
    public TrainDTO updateTrainSeats(Long trainId, Integer seatsChange) {
        Train train = trainRepository.findById(trainId)
            .orElseThrow(() -> new RuntimeException("Train not found with id: " + trainId));
        
        int newAvailableSeats = train.getAvailableSeats() + seatsChange;
        if (newAvailableSeats < 0 || newAvailableSeats > train.getTotalSeats()) {
            throw new RuntimeException("Invalid seats change");
        }
        
        train.setAvailableSeats(newAvailableSeats);
        Train updatedTrain = trainRepository.save(train);
        return mapToDTO(updatedTrain);
    }
    
    @Override
    public List<TrainDTO> getTrainsWithAvailableSeats(Integer minSeats) {
        return trainRepository.findTrainsWithAvailableSeats(minSeats).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    private Train mapToEntity(TrainDTO dto) {
        Train train = new Train();
        train.setId(dto.getId());
        train.setTrainNumber(dto.getTrainNumber());
        train.setTrainName(dto.getTrainName());
        train.setDepartureTime(dto.getDepartureTime());
        train.setArrivalTime(dto.getArrivalTime());
        train.setTotalSeats(dto.getTotalSeats());
        train.setAvailableSeats(dto.getAvailableSeats());
        train.setBasePrice(dto.getBasePrice());
        train.setTrainType(dto.getTrainType());
        train.setStatus(dto.getStatus());
        
        // Set stations
        if (dto.getSourceStation() != null && dto.getSourceStation().getId() != null) {
            Station sourceStation = stationRepository.findById(dto.getSourceStation().getId())
                .orElseThrow(() -> new RuntimeException("Source station not found"));
            train.setSourceStation(sourceStation);
        }
        
        if (dto.getDestinationStation() != null && dto.getDestinationStation().getId() != null) {
            Station destStation = stationRepository.findById(dto.getDestinationStation().getId())
                .orElseThrow(() -> new RuntimeException("Destination station not found"));
            train.setDestinationStation(destStation);
        }
        
        return train;
    }
    
    private TrainDTO mapToDTO(Train train) {
        TrainDTO dto = new TrainDTO();
        dto.setId(train.getId());
        dto.setTrainNumber(train.getTrainNumber());
        dto.setTrainName(train.getTrainName());
        dto.setDepartureTime(train.getDepartureTime());
        dto.setArrivalTime(train.getArrivalTime());
        dto.setTotalSeats(train.getTotalSeats());
        dto.setAvailableSeats(train.getAvailableSeats());
        dto.setBasePrice(train.getBasePrice());
        dto.setTrainType(train.getTrainType());
        dto.setStatus(train.getStatus());
        
        // Map source station
        if (train.getSourceStation() != null) {
            Station sourceStation = train.getSourceStation();
            StationDTO sourceStationDTO = new StationDTO(
                sourceStation.getId(),
                sourceStation.getCode(),
                sourceStation.getName(),
                sourceStation.getCity(),
                sourceStation.getState(),
                sourceStation.getPlatformCount(),
                sourceStation.getFacilities()
            );
            dto.setSourceStation(sourceStationDTO);
        }
        
        // Map destination station
        if (train.getDestinationStation() != null) {
            Station destStation = train.getDestinationStation();
            StationDTO destStationDTO = new StationDTO(
                destStation.getId(),
                destStation.getCode(),
                destStation.getName(),
                destStation.getCity(),
                destStation.getState(),
                destStation.getPlatformCount(),
                destStation.getFacilities()
            );
            dto.setDestinationStation(destStationDTO);
        }
        
        // Calculate travel duration
        if (train.getDepartureTime() != null && train.getArrivalTime() != null) {
            Duration duration = Duration.between(train.getDepartureTime(), train.getArrivalTime());
            dto.setTravelDuration(duration.toMinutes());
        }
        
        return dto;
    }
}