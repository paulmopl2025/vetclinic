package com.example.vetclinic.application.dto.clinic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClinicStatsDTO {
    private Long totalAppointments;
    private Long pendingAppointments;
    private Long confirmedAppointments;
    private Long completedAppointments;
    private Long cancelledAppointments;
    private Long totalPets;
    private Long totalOwners;
    private Long totalVets;
    private Long totalMedicalRecords;
    private BigDecimal estimatedRevenue;
}
