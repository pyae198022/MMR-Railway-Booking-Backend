package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "passengers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @OneToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private Integer age;
    
    @Column(nullable = false)
    private String gender; // MALE, FEMALE, OTHER
    
    @Column(nullable = false)
    private String idType; // AADHAR, PASSPORT, DL, VOTER_ID
    
    @Column(nullable = false)
    private String idNumber;
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    @Column(nullable = false)
    private String berthPreference; // LOWER, MIDDLE, UPPER, SIDE_LOWER, SIDE_UPPER
    
    @Column(nullable = false)
    private Double fare;
    
    @Column(nullable = false)
    private String status = "CONFIRMED"; // CONFIRMED, WAITLIST, RAC, CANCELLED
}