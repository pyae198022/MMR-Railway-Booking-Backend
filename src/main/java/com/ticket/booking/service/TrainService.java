package com.ticket.booking.service;

import com.ticket.booking.dto.TrainDTO;
import com.ticket.booking.dto.TrainSearchRequest;
import com.ticket.booking.dto.TrainSearchResponse;
import java.util.List;

public interface TrainService {
    
    TrainDTO createTrain(TrainDTO trainDTO);
    
    TrainDTO updateTrain(Long id, TrainDTO trainDTO);
    
    void deleteTrain(Long id);
    
    TrainDTO getTrainById(Long id);
    
    TrainDTO getTrainByNumber(String trainNumber);
    
    List<TrainDTO> getAllTrains();
    
    List<TrainDTO> getTrainsByType(String trainType);
    
    List<TrainDTO> getActiveTrains();
    
    List<TrainSearchResponse> searchTrains(TrainSearchRequest searchRequest);
    
    TrainDTO updateTrainSeats(Long trainId, Integer seatsChange);
    
    List<TrainDTO> getTrainsWithAvailableSeats(Integer minSeats);
}