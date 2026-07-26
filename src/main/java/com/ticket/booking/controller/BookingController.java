package com.ticket.booking.controller;

import com.ticket.booking.dto.BookingRequestDTO;
import com.ticket.booking.dto.BookingResponseDTO;
import com.ticket.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    
    private final BookingService bookingService;
    
    @PostMapping
    public ResponseEntity<BookingResponseDTO> createBooking(@RequestBody BookingRequestDTO bookingRequest) {
        BookingResponseDTO createdBooking = bookingService.createBooking(bookingRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        BookingResponseDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }
    
    @GetMapping("/pnr/{pnrNumber}")
    public ResponseEntity<BookingResponseDTO> getBookingByPnr(@PathVariable String pnrNumber) {
        BookingResponseDTO booking = bookingService.getBookingByPnr(pnrNumber);
        return ResponseEntity.ok(booking);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Long userId) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long bookingId) {
        BookingResponseDTO cancelledBooking = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(cancelledBooking);
    }
    
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<BookingResponseDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam String status) {
        BookingResponseDTO updatedBooking = bookingService.updateBookingStatus(bookingId, status);
        return ResponseEntity.ok(updatedBooking);
    }
    
    @PatchMapping("/{bookingId}/payment-status")
    public ResponseEntity<BookingResponseDTO> updatePaymentStatus(
            @PathVariable Long bookingId,
            @RequestParam String paymentStatus) {
        BookingResponseDTO updatedBooking = bookingService.updatePaymentStatus(bookingId, paymentStatus);
        return ResponseEntity.ok(updatedBooking);
    }
    
    @PostMapping("/calculate-fare")
    public ResponseEntity<Double> calculateFare(
            @RequestParam Long trainId,
            @RequestParam List<Long> seatIds) {
        Double fare = bookingService.calculateFare(trainId, seatIds);
        return ResponseEntity.ok(fare);
    }
}