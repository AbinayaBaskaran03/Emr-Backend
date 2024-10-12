package com.emrheathgroup.backend.DTOs.AdminDTOs;

import java.sql.Time;
import java.util.Date;

import com.emrheathgroup.backend.Enumeration.ExistsResponse;
import com.emrheathgroup.backend.Enumeration.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobPatientTimeSlotDto {

	private ExistsResponse existsResponse;
	private String patientName;

	private String phoneNo;
	private String email;

	private Date dob;
	private Gender gender;
	private Integer age;

	private Time slotTiming;
	private String mrdNo;
}
