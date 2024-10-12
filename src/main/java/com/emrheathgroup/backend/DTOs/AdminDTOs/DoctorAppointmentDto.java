
package com.emrheathgroup.backend.DTOs.AdminDTOs;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DoctorAppointmentDto {

	private Date appointmentDate;

	private Integer branchId;

	private Integer specialityId; 
	private String specialization;
	
	private Integer docId;
	private String docName;
	
	private Time startTime;
	private Time endTime;

	private String scheduleType;
	private List<PatientTimeSlotDto> appointments;
	private List<MobPatientTimeSlotDto> mobPatientTimeSlotDto;

}
