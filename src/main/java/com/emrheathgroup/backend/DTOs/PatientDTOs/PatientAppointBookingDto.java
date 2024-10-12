package com.emrheathgroup.backend.DTOs.PatientDTOs;

import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointBookingDto {

	private Integer branchId;
	private Integer docId;
	
	private Integer patientId;
	private Time appointmentTime;

	private String appointmentType;
	private String meetingType;
	private String reason;

}
