package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String idType;
    private String idNumber;
    private LocalDate dateOfBirth;
    private String berthPreference;
    private Double fare;
    private String status;
    private SeatDTO seat;
}