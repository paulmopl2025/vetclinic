package com.example.vetclinic.application.dto.clinic;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ClinicDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private Integer maxDailyAppointments;
    private Boolean active;
}
