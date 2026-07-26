package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String pnrNumber;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;
    
    @ManyToOne
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station sourceStation;
    
    @ManyToOne
    @JoinColumn(name = "destination_station_id", nullable = false)
    private Station destinationStation;
    
    @Column(nullable = false)
    private LocalDateTime bookingDate = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime journeyDate;
    
    @Column(nullable = false)
    private Integer numberOfPassengers;
    
    @Column(nullable = false)
    private Double totalFare;
    
    @Column(nullable = false)
    private Double taxAmount;
    
    @Column(nullable = false)
    private Double grandTotal;
    
    @Column(nullable = false)
    private String bookingStatus = "CONFIRMED"; // CONFIRMED, CANCELLED, WAITLIST
    
    @Column(nullable = false)
    private String paymentStatus = "PENDING"; // PENDING, COMPLETED, FAILED, REFUNDED
    
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Passenger> passengers = new ArrayList<>();
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;
}