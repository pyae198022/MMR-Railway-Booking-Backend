package com.ticket.booking.repository;

import com.ticket.booking.model.Booking;
import com.ticket.booking.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    
    List<Passenger> findByBooking(Booking booking);
    
    List<Passenger> findByBookingAndStatus(Booking booking, String status);
}