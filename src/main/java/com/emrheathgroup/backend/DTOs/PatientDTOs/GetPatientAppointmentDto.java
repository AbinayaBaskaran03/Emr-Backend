package com.emrheathgroup.backend.DTOs.PatientDTOs;

import java.sql.Time;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPatientAppointmentDto {

	private Integer patientId;
	private String patientName;
	private String problem;
	private Date appointmentDate;
	private Time appointmentTime;
	private String location;
}
