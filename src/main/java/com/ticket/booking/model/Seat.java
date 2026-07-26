package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;
    
    @Column(nullable = false)
    private String seatNumber;
    
    @Column(nullable = false)
    private String coachType; // AC, Sleeper, General, etc.
    
    @Column(nullable = false)
    private String seatType; // Window, Middle, Aisle, etc.
    
    @Column(nullable = false)
    private Double priceMultiplier = 1.0;
    
    @Column(nullable = false)
    private Boolean isAvailable = true;
    
    @Column(nullable = false)
    private String status = "AVAILABLE"; // AVAILABLE, BOOKED, BLOCKED
    
    @OneToOne(mappedBy = "seat")
    private Passenger passenger;
}