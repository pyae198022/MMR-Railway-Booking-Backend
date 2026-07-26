package com.ticket.booking.repository;

import com.ticket.booking.model.Booking;
import com.ticket.booking.model.Train;
import com.ticket.booking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    Optional<Booking> findByPnrNumber(String pnrNumber);
    
    List<Booking> findByUser(User user);
    
    List<Booking> findByTrain(Train train);
    
    List<Booking> findByBookingStatus(String bookingStatus);
    
    List<Booking> findByPaymentStatus(String paymentStatus);
    
    List<Booking> findByJourneyDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    List<Booking> findByUserAndBookingStatus(User user, String bookingStatus);
    
    long countByTrainAndJourneyDate(Train train, LocalDateTime journeyDate);
}