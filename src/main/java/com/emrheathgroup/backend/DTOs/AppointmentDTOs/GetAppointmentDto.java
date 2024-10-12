package com.emrheathgroup.backend.DTOs.AppointmentDTOs;

import java.sql.Time;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAppointmentDto {

	private Integer appointmentId;
	private Date appointmentDate;
	private Time appointmentTime;
	private String patientName; 
	private String speciality; 
	private String appointmentStatus;
	private String status; 

}
