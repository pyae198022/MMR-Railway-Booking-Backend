package com.ticket.booking.service;

import com.ticket.booking.dto.StationDTO;
import com.ticket.booking.model.Station;
import java.util.List;

public interface StationService {
    
    StationDTO createStation(StationDTO stationDTO);
    
    StationDTO updateStation(Long id, StationDTO stationDTO);
    
    void deleteStation(Long id);
    
    StationDTO getStationById(Long id);
    
    StationDTO getStationByCode(String code);
    
    List<StationDTO> getAllStations();
    
    List<StationDTO> getStationsByCity(String city);
    
    List<StationDTO> getStationsByState(String state);
    
    List<StationDTO> searchStationsByName(String name);
}