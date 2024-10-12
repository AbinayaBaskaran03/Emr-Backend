package com.emrheathgroup.backend.DTOs.AdminDTOs;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotDto {

	private Time slotTiming;

}
