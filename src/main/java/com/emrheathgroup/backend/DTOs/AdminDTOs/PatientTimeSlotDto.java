package com.emrheathgroup.backend.DTOs.AdminDTOs;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientTimeSlotDto {
	
	private String patientName;
	private Time slotTiming;
	private Integer patientId;


}
