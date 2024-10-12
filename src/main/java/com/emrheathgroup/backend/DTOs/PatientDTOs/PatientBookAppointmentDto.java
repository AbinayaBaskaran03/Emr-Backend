package com.emrheathgroup.backend.DTOs.PatientDTOs;

import java.sql.Time;
import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientBookAppointmentDto {

	private Integer patientId;
	private Integer doctorId;
	private Date appointmentDate;
	private Time appointmentTime;
	private String appointmentType; 
	private String meetingType; 
	private String reason;
	private MultipartFile attachment;

}
