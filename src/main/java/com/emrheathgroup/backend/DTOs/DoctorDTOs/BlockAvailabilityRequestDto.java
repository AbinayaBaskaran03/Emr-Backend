package com.emrheathgroup.backend.DTOs.DoctorDTOs;

import java.sql.Time;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockAvailabilityRequestDto {

	private Integer branchId;
	private Date date; 
	private Time startTime;
	private Time endTime;
	private String title;
	
	private Integer rosterId;
	private Integer doctorId;

}
