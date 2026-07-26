package com.ticket.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;
    
    @Column(nullable = false, unique = true)
    private String transactionId;
    
    @Column(nullable = false)
    private Double amount;
    
    @Column(nullable = false)
    private String paymentMethod; // CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, WALLET
    
    @Column(nullable = false)
    private String paymentStatus = "PENDING"; // PENDING, SUCCESS, FAILED, REFUNDED
    
    @Column(nullable = false)
    private LocalDateTime paymentDate = LocalDateTime.now();
    
    private String bankName;
    
    private String cardLastFour;
    
    private String upiId;
    
    @Column(columnDefinition = "TEXT")
    private String paymentResponse;
}