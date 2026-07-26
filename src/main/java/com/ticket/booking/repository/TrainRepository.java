package com.ticket.booking.repository;

import com.ticket.booking.model.Station;
import com.ticket.booking.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    
    Optional<Train> findByTrainNumber(String trainNumber);
    
    List<Train> findByTrainType(String trainType);
    
    List<Train> findBySourceStationAndDestinationStationAndDepartureTimeBetween(
        Station sourceStation, 
        Station destinationStation,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
    
    List<Train> findByStatus(String status);
    
    @Query("SELECT t FROM Train t WHERE t.sourceStation.city = :sourceCity AND t.destinationStation.city = :destCity " +
           "AND t.departureTime >= :startOfDay AND t.departureTime < :nextDay")
    List<Train> findTrainsBetweenCitiesOnDate(
        @Param("sourceCity") String sourceCity,
        @Param("destCity") String destCity,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("nextDay") LocalDateTime nextDay
    );
    
    @Query("SELECT t FROM Train t WHERE t.availableSeats >= :minSeats")
    List<Train> findTrainsWithAvailableSeats(@Param("minSeats") Integer minSeats);
}