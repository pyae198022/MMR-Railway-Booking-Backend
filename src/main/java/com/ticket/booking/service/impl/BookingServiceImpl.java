package com.ticket.booking.service.impl;

import com.ticket.booking.dto.BookingRequestDTO;
import com.ticket.booking.dto.BookingResponseDTO;
import com.ticket.booking.dto.PassengerRequestDTO;
import com.ticket.booking.model.*;
import com.ticket.booking.repository.*;
import com.ticket.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final SeatRepository seatRepository;
    private final PassengerRepository passengerRepository;
    
    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequest) {
        // Validate and get required entities
        User user = userRepository.findById(bookingRequest.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Train train = trainRepository.findById(bookingRequest.getTrainId())
            .orElseThrow(() -> new RuntimeException("Train not found"));
        
        Station sourceStation = stationRepository.findById(bookingRequest.getSourceStationId())
            .orElseThrow(() -> new RuntimeException("Source station not found"));
        
        Station destinationStation = stationRepository.findById(bookingRequest.getDestinationStationId())
            .orElseThrow(() -> new RuntimeException("Destination station not found"));
        
        // Check if train has enough seats
        if (train.getAvailableSeats() < bookingRequest.getPassengers().size()) {
            throw new RuntimeException("Not enough seats available");
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setPnrNumber(generatePnrNumber());
        booking.setUser(user);
        booking.setTrain(train);
        booking.setSourceStation(sourceStation);
        booking.setDestinationStation(destinationStation);
        booking.setJourneyDate(bookingRequest.getJourneyDate());
        booking.setNumberOfPassengers(bookingRequest.getPassengers().size());
        
        // Calculate fare (simplified)
        double baseFare = train.getBasePrice() * bookingRequest.getPassengers().size();
        double tax = baseFare * 0.18; // 18% GST
        double totalFare = baseFare + tax;
        
        booking.setTotalFare(baseFare);
        booking.setTaxAmount(tax);
        booking.setGrandTotal(totalFare);
        booking.setBookingStatus("CONFIRMED");
        booking.setPaymentStatus("PENDING");
        
        Booking savedBooking = bookingRepository.save(booking);
        
        // Create passengers
        List<Passenger> passengers = bookingRequest.getPassengers().stream()
            .map(passengerRequest -> createPassenger(passengerRequest, savedBooking))
            .collect(Collectors.toList());
        
        passengerRepository.saveAll(passengers);
        
        // Update train seat availability
        train.setAvailableSeats(train.getAvailableSeats() - bookingRequest.getPassengers().size());
        trainRepository.save(train);
        
        return mapToDTO(savedBooking);
    }
    
    @Override
    public BookingResponseDTO getBookingByPnr(String pnrNumber) {
        Booking booking = bookingRepository.findByPnrNumber(pnrNumber)
            .orElseThrow(() -> new RuntimeException("Booking not found with PNR: " + pnrNumber));
        return mapToDTO(booking);
    }
    
    @Override
    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
        return mapToDTO(booking);
    }
    
    @Override
    public List<BookingResponseDTO> getBookingsByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return bookingRepository.findByUser(user).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public BookingResponseDTO cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setBookingStatus("CANCELLED");
        Booking cancelledBooking = bookingRepository.save(booking);
        
        // Update train seat availability
        Train train = booking.getTrain();
        train.setAvailableSeats(train.getAvailableSeats() + booking.getNumberOfPassengers());
        trainRepository.save(train);
        
        return mapToDTO(cancelledBooking);
    }
    
    @Override
    public BookingResponseDTO updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setBookingStatus(status);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDTO(updatedBooking);
    }
    
    @Override
    public BookingResponseDTO updatePaymentStatus(Long bookingId, String paymentStatus) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        
        booking.setPaymentStatus(paymentStatus);
        Booking updatedBooking = bookingRepository.save(booking);
        return mapToDTO(updatedBooking);
    }
    
    @Override
    public Double calculateFare(Long trainId, List<Long> seatIds) {
        Train train = trainRepository.findById(trainId)
            .orElseThrow(() -> new RuntimeException("Train not found"));
        
        double baseFare = train.getBasePrice() * seatIds.size();
        double tax = baseFare * 0.18;
        return baseFare + tax;
    }
    
    private Passenger createPassenger(PassengerRequestDTO passengerRequest, Booking booking) {
        Passenger passenger = new Passenger();
        passenger.setBooking(booking);
        passenger.setFirstName(passengerRequest.getFirstName());
        passenger.setLastName(passengerRequest.getLastName());
        passenger.setAge(passengerRequest.getAge());
        passenger.setGender(passengerRequest.getGender());
        passenger.setIdType(passengerRequest.getIdType());
        passenger.setIdNumber(passengerRequest.getIdNumber());
        passenger.setDateOfBirth(passengerRequest.getDateOfBirth());
        passenger.setBerthPreference(passengerRequest.getBerthPreference());
        
        // Assign seat if specified
        if (passengerRequest.getSeatId() != null) {
            Seat seat = seatRepository.findById(passengerRequest.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));
            passenger.setSeat(seat);
            passenger.setFare(booking.getTrain().getBasePrice() * seat.getPriceMultiplier());
        } else {
            passenger.setFare(booking.getTrain().getBasePrice());
        }
        
        passenger.setStatus("CONFIRMED");
        return passenger;
    }
    
    private String generatePnrNumber() {
        return "PNR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private BookingResponseDTO mapToDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setPnrNumber(booking.getPnrNumber());
        dto.setBookingDate(booking.getBookingDate());
        dto.setJourneyDate(booking.getJourneyDate());
        dto.setNumberOfPassengers(booking.getNumberOfPassengers());
        dto.setTotalFare(booking.getTotalFare());
        dto.setTaxAmount(booking.getTaxAmount());
        dto.setGrandTotal(booking.getGrandTotal());
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setPaymentStatus(booking.getPaymentStatus());
        
        return dto;
    }
}