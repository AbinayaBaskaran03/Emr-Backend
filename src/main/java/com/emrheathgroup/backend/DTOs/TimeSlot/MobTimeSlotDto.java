package com.emrheathgroup.backend.DTOs.TimeSlot;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobTimeSlotDto {

	private Time startTime;
	private Time endTime;

}
