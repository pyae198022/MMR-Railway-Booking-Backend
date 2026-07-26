package com.ticket.booking.repository;

import com.ticket.booking.model.Seat;
import com.ticket.booking.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    
    List<Seat> findByTrainAndIsAvailableTrue(Train train);
    
    List<Seat> findByTrainAndCoachTypeAndIsAvailableTrue(Train train, String coachType);
    
    Optional<Seat> findByTrainAndSeatNumber(Train train, String seatNumber);
    
    List<Seat> findByTrain(Train train);
    
    List<Seat> findByTrainAndStatus(Train train, String status);
    
    long countByTrainAndIsAvailableTrue(Train train);
}