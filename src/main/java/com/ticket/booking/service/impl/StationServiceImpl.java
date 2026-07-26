package com.ticket.booking.service.impl;

import com.ticket.booking.dto.StationDTO;
import com.ticket.booking.model.Station;
import com.ticket.booking.repository.StationRepository;
import com.ticket.booking.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {
    
    private final StationRepository stationRepository;
    
    @Override
    public StationDTO createStation(StationDTO stationDTO) {
        Station station = mapToEntity(stationDTO);
        Station savedStation = stationRepository.save(station);
        return mapToDTO(savedStation);
    }
    
    @Override
    public StationDTO updateStation(Long id, StationDTO stationDTO) {
        Station existingStation = stationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
        
        existingStation.setCode(stationDTO.getCode());
        existingStation.setName(stationDTO.getName());
        existingStation.setCity(stationDTO.getCity());
        existingStation.setState(stationDTO.getState());
        existingStation.setPlatformCount(stationDTO.getPlatformCount());
        existingStation.setFacilities(stationDTO.getFacilities());
        
        Station updatedStation = stationRepository.save(existingStation);
        return mapToDTO(updatedStation);
    }
    
    @Override
    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }
    
    @Override
    public StationDTO getStationById(Long id) {
        Station station = stationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Station not found with id: " + id));
        return mapToDTO(station);
    }
    
    @Override
    public StationDTO getStationByCode(String code) {
        Station station = stationRepository.findByCode(code)
            .orElseThrow(() -> new RuntimeException("Station not found with code: " + code));
        return mapToDTO(station);
    }
    
    @Override
    public List<StationDTO> getAllStations() {
        return stationRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<StationDTO> getStationsByCity(String city) {
        return stationRepository.findByCity(city).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<StationDTO> getStationsByState(String state) {
        return stationRepository.findByState(state).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<StationDTO> searchStationsByName(String name) {
        return stationRepository.findByNameContainingIgnoreCase(name).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    private Station mapToEntity(StationDTO dto) {
        Station station = new Station();
        station.setId(dto.getId());
        station.setCode(dto.getCode());
        station.setName(dto.getName());
        station.setCity(dto.getCity());
        station.setState(dto.getState());
        station.setPlatformCount(dto.getPlatformCount());
        station.setFacilities(dto.getFacilities());
        return station;
    }
    
    private StationDTO mapToDTO(Station station) {
        return new StationDTO(
            station.getId(),
            station.getCode(),
            station.getName(),
            station.getCity(),
            station.getState(),
            station.getPlatformCount(),
            station.getFacilities()
        );
    }
}