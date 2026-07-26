package com.ticket.booking.service;

import com.ticket.booking.dto.BookingRequestDTO;
import com.ticket.booking.dto.BookingResponseDTO;
import java.util.List;

public interface BookingService {
    
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequest);
    
    BookingResponseDTO getBookingByPnr(String pnrNumber);
    
    BookingResponseDTO getBookingById(Long id);
    
    List<BookingResponseDTO> getBookingsByUserId(Long userId);
    
    List<BookingResponseDTO> getAllBookings();
    
    BookingResponseDTO cancelBooking(Long bookingId);
    
    BookingResponseDTO updateBookingStatus(Long bookingId, String status);
    
    BookingResponseDTO updatePaymentStatus(Long bookingId, String paymentStatus);
    
    Double calculateFare(Long trainId, List<Long> seatIds);
}