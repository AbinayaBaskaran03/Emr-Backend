package com.emrheathgroup.backend.DTOs.PatientDTOs;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimeSlotDto {
	private Time time;
}
