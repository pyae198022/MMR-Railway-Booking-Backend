package com.ticket.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String state;
    private String platformCount;
    private String facilities;
}