package com.ticket.booking.controller;

import com.ticket.booking.dto.TrainDTO;
import com.ticket.booking.dto.TrainSearchRequest;
import com.ticket.booking.dto.TrainSearchResponse;
import com.ticket.booking.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/trains")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TrainController {
    
    private final TrainService trainService;
    
    @PostMapping
    public ResponseEntity<TrainDTO> createTrain(@RequestBody TrainDTO trainDTO) {
        TrainDTO createdTrain = trainService.createTrain(trainDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTrain);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TrainDTO> updateTrain(@PathVariable Long id, @RequestBody TrainDTO trainDTO) {
        TrainDTO updatedTrain = trainService.updateTrain(id, trainDTO);
        return ResponseEntity.ok(updatedTrain);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrainById(@PathVariable Long id) {
        TrainDTO train = trainService.getTrainById(id);
        return ResponseEntity.ok(train);
    }
    
    @GetMapping("/number/{trainNumber}")
    public ResponseEntity<TrainDTO> getTrainByNumber(@PathVariable String trainNumber) {
        TrainDTO train = trainService.getTrainByNumber(trainNumber);
        return ResponseEntity.ok(train);
    }
    
    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        List<TrainDTO> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/type/{trainType}")
    public ResponseEntity<List<TrainDTO>> getTrainsByType(@PathVariable String trainType) {
        List<TrainDTO> trains = trainService.getTrainsByType(trainType);
        return ResponseEntity.ok(trains);
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<TrainDTO>> getActiveTrains() {
        List<TrainDTO> trains = trainService.getActiveTrains();
        return ResponseEntity.ok(trains);
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<TrainSearchResponse>> searchTrains(@RequestBody TrainSearchRequest searchRequest) {
        List<TrainSearchResponse> results = trainService.searchTrains(searchRequest);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/available-seats")
    public ResponseEntity<List<TrainDTO>> getTrainsWithAvailableSeats(@RequestParam(defaultValue = "1") Integer minSeats) {
        List<TrainDTO> trains = trainService.getTrainsWithAvailableSeats(minSeats);
        return ResponseEntity.ok(trains);
    }
    
    @PatchMapping("/{trainId}/seats")
    public ResponseEntity<TrainDTO> updateTrainSeats(
            @PathVariable Long trainId, 
            @RequestParam Integer seatsChange) {
        TrainDTO updatedTrain = trainService.updateTrainSeats(trainId, seatsChange);
        return ResponseEntity.ok(updatedTrain);
    }
}