package com.emrheathgroup.backend.DTOs.AppointmentDTOs;

import java.sql.Time;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRescheduleDto {
	
	private Integer appointmentId;
	private Date appointmentDate;
    private Time appointmentTime;
    private Integer doctorId;


}
